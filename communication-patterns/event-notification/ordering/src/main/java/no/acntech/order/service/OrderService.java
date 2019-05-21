package no.acntech.order.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import no.acntech.order.exception.ItemAlreadyExistsException;
import no.acntech.order.exception.ItemNotFoundException;
import no.acntech.order.exception.OrderNotFoundException;
import no.acntech.order.exception.ReservationIdNotReceivedException;
import no.acntech.order.model.CreateItemDto;
import no.acntech.order.model.CreateOrderDto;
import no.acntech.order.model.DeleteItemDto;
import no.acntech.order.model.Item;
import no.acntech.order.model.Order;
import no.acntech.order.model.OrderDto;
import no.acntech.order.model.OrderQuery;
import no.acntech.order.model.OrderStatus;
import no.acntech.order.model.UpdateItemDto;
import no.acntech.order.producer.OrderEventProducer;
import no.acntech.order.repository.ItemRepository;
import no.acntech.order.repository.OrderRepository;
import no.acntech.reservation.consumer.ReservationRestConsumer;
import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.PendingReservationDto;
import no.acntech.reservation.model.UpdateReservationDto;

@SuppressWarnings("Duplicates")
@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    private static final Sort SORT_BY_ID = Sort.by("id");

    private final ConversionService conversionService;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final OrderEventProducer orderEventProducer;
    private final ReservationRestConsumer reservationRestConsumer;

    public OrderService(final ConversionService conversionService,
                        final OrderRepository orderRepository,
                        final ItemRepository itemRepository,
                        final OrderEventProducer orderEventProducer,
                        final ReservationRestConsumer reservationRestConsumer) {
        this.conversionService = conversionService;
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.orderEventProducer = orderEventProducer;
        this.reservationRestConsumer = reservationRestConsumer;
    }

    public List<OrderDto> findOrders(@NotNull final OrderQuery orderQuery) {
        UUID customerId = orderQuery.getCustomerId();
        OrderStatus status = orderQuery.getStatus();

        if (customerId != null && status != null) {
            return orderRepository.findAllByCustomerIdAndStatus(customerId, status)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else if (customerId != null) {
            return orderRepository.findAllByCustomerId(customerId)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else if (status != null) {
            return orderRepository.findAllByStatus(status)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else {
            return orderRepository.findAll(SORT_BY_ID)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        }
    }

    public OrderDto getOrder(@NotNull final UUID orderId) {
        Order order = getOrderByOrderId(orderId);
        return convert(order);
    }

    @Transactional
    public OrderDto createOrder(@Valid final CreateOrderDto createOrder) {
        Order order = conversionService.convert(createOrder, Order.class);
        Assert.notNull(order, "Failed to convert order");

        Order createdOrder = orderRepository.save(order);

        LOGGER.debug("Created order with order-id {}", createdOrder.getOrderId());
        orderEventProducer.publish(createdOrder.getOrderId());

        return convert(createdOrder);
    }

    @Transactional
    public OrderDto updateOrder(@NotNull final UUID orderId) {
        Order order = getOrderByOrderId(orderId);

        order.getItems().stream()
                .map(Item::getReservationId)
                .forEach(this::confirmReservation);

        LOGGER.debug("Updated order with order-id {}", orderId);
        orderEventProducer.publish(orderId);

        return convert(order);
    }

    private void confirmReservation(UUID reservationId) {
        final UpdateReservationDto updateReservation = UpdateReservationDto.builder()
                .statusConfirmed()
                .build();

        LOGGER.debug("Updating reservation status to {} for reservation-id {}", updateReservation.getStatus().name(), reservationId);
        reservationRestConsumer.update(reservationId, updateReservation);
    }

    @Transactional
    public OrderDto deleteOrder(@NotNull final UUID orderId) {
        Order order = getOrderByOrderId(orderId);

        order.getItems().stream()
                .map(Item::getReservationId)
                .forEach(reservationRestConsumer::delete);

        LOGGER.debug("Delete order with order-id {}", orderId);
        orderEventProducer.publish(orderId);

        return convert(order);
    }

    @Transactional
    public OrderDto createItem(@NotNull final UUID orderId, @Valid final CreateItemDto createItem) {
        UUID productId = createItem.getProductId();
        Long quantity = createItem.getQuantity();

        Order order = getOrderByOrderId(orderId);
        Optional<Item> exitingItem = itemRepository.findByOrderIdAndProductId(order.getId(), productId);

        if (!exitingItem.isPresent()) {
            CreateReservationDto createReservation = CreateReservationDto.builder()
                    .orderId(orderId)
                    .productId(productId)
                    .quantity(quantity)
                    .build();

            Optional<PendingReservationDto> pendingReservationOptional = reservationRestConsumer.create(createReservation);

            if (pendingReservationOptional.isPresent()) {
                PendingReservationDto pendingReservation = pendingReservationOptional.get();

                Item item = Item.builder()
                        .orderId(order.getId())
                        .productId(productId)
                        .reservationId(pendingReservation.getReservationId())
                        .quantity(quantity)
                        .build();

                itemRepository.save(item);

                LOGGER.debug("Created order item with product-id {} for order-id {}", orderId, productId);
                orderEventProducer.publish(orderId);

                Order updatedOrder = getOrderByOrderId(orderId);
                return convert(updatedOrder);
            } else {
                throw new ReservationIdNotReceivedException(orderId, productId);
            }
        } else {
            throw new ItemAlreadyExistsException(orderId, productId);
        }
    }

    @Transactional
    public OrderDto updateItem(@NotNull final UUID orderId, @Valid final UpdateItemDto updateItem) {
        UUID productId = updateItem.getProductId();

        Order order = getOrderByOrderId(orderId);
        Item item = getItemByOrderAndProductId(order, productId);

        UUID reservationId = item.getReservationId();
        Long quantity = updateItem.getQuantity();

        UpdateReservationDto updateReservation = UpdateReservationDto.builder()
                .quantity(quantity)
                .build();
        reservationRestConsumer.update(reservationId, updateReservation);

        LOGGER.debug("Updated order item for order-id {} and product-id {}", orderId, productId);
        orderEventProducer.publish(orderId);

        return convert(order);
    }

    @Transactional
    public OrderDto deleteItem(@NotNull final UUID orderId, @Valid final DeleteItemDto deleteItemDto) {
        UUID productId = deleteItemDto.getProductId();

        Order order = getOrderByOrderId(orderId);
        Item item = getItemByOrderAndProductId(order, productId);

        UUID reservationId = item.getReservationId();

        reservationRestConsumer.delete(reservationId);

        LOGGER.debug("Deleted order item with product-id {} for order-id {}", orderId, productId);
        orderEventProducer.publish(orderId);

        return convert(order);
    }

    private Order getOrderByOrderId(final UUID orderId) {
        return orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    private Item getItemByOrderAndProductId(final Order order, final UUID productId) {
        return itemRepository.findByOrderIdAndProductId(order.getId(), productId)
                .orElseThrow(() -> new ItemNotFoundException(order.getOrderId(), productId));
    }

    private OrderDto convert(final Order order) {
        return conversionService.convert(order, OrderDto.class);
    }
}
