package no.acntech.order.service;

import no.acntech.order.exception.ItemAlreadyExistsException;
import no.acntech.order.exception.ItemNotFoundException;
import no.acntech.order.exception.OrderNotFoundException;
import no.acntech.order.model.*;
import no.acntech.order.producer.OrderEventProducer;
import no.acntech.order.repository.ItemRepository;
import no.acntech.order.repository.OrderRepository;
import no.acntech.reservation.consumer.ReservationRestConsumer;
import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.PendingReservationDto;
import no.acntech.reservation.model.UpdateReservationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

        Order createdOrder = orderRepository.save(order);

        LOGGER.debug("Created order with order-id {}", createdOrder.getOrderId());
        orderEventProducer.publish(createdOrder.getOrderId());

        return convert(createdOrder);
    }

    @Transactional
    public OrderDto updateOrder(@NotNull final UUID orderId) {
        OrderDto order = getOrder(orderId);

        order.getItems().stream()
                .map(ItemDto::getReservationId)
                .forEach(reservationId -> {
                    final UpdateReservationDto updateReservation = UpdateReservationDto.builder()
                            .statusConfirmed()
                            .build();
                    reservationRestConsumer.update(reservationId, updateReservation);
                });

        LOGGER.debug("Updated order with order-id {}", orderId);
        orderEventProducer.publish(orderId);

        return order;
    }

    @Transactional
    public OrderDto deleteOrder(@NotNull final UUID orderId) {
        OrderDto order = getOrder(orderId);

        order.getItems().stream()
                .map(ItemDto::getReservationId)
                .forEach(reservationRestConsumer::delete);

        LOGGER.debug("Delete order with order-id {}", orderId);
        orderEventProducer.publish(orderId);

        return order;
    }

    @Transactional
    public OrderDto createItem(@NotNull final UUID orderId, @Valid final CreateItemDto createItem) {
        UUID productId = createItem.getProductId();
        Long quantity = createItem.getQuantity();

        Order order = getOrderByOrderId(orderId);
        Optional<Item> exitingItem = itemRepository.findByOrderIdAndProductId(order.getId(), productId);

        if (!exitingItem.isPresent()) {
            Item item = Item.builder()
                    .orderId(order.getId())
                    .productId(productId)
                    .quantity(quantity)
                    .build();

            Item savedItem = itemRepository.save(item);

            CreateReservationDto createReservation = CreateReservationDto.builder()
                    .orderId(orderId)
                    .productId(productId)
                    .quantity(quantity)
                    .build();
            Optional<PendingReservationDto> pendingReservationOptional = reservationRestConsumer.create(createReservation);

            if (pendingReservationOptional.isPresent()) {
                PendingReservationDto pendingReservation = pendingReservationOptional.get();
                savedItem.setReservationId(pendingReservation.getReservationId());
            }

            LOGGER.debug("Created order item with product-id {} for order-id {}", orderId, productId);
            orderEventProducer.publish(orderId);

            return convert(order);
        } else {
            throw new ItemAlreadyExistsException(orderId, productId);
        }
    }

    @Transactional
    public OrderDto updateItem(@NotNull final UUID orderId, @Valid final UpdateItemDto updateItem) {
        UUID productId = updateItem.getProductId();

        final Order order = getOrderByOrderId(orderId);
        final Optional<Item> exitingItem = itemRepository.findByOrderIdAndProductId(order.getId(), productId);

        if (exitingItem.isPresent()) {
            final Item item = exitingItem.get();
            final UUID reservationId = item.getReservationId();
            final Long quantity = updateItem.getQuantity();

            final UpdateReservationDto updateReservation = UpdateReservationDto.builder()
                    .quantity(quantity)
                    .build();
            reservationRestConsumer.update(reservationId, updateReservation);

            LOGGER.debug("Updated order item for order-id {} and product-id {}", orderId, productId);
            orderEventProducer.publish(orderId);

            return convert(order);
        } else {
            throw new ItemNotFoundException(orderId, productId);
        }
    }

    @Transactional
    public OrderDto deleteItem(@NotNull final UUID orderId, @Valid final DeleteItemDto deleteItemDto) {
        UUID productId = deleteItemDto.getProductId();

        final Order order = getOrderByOrderId(orderId);
        final Optional<Item> exitingItem = itemRepository.findByOrderIdAndProductId(order.getId(), productId);

        if (exitingItem.isPresent()) {
            final Item item = exitingItem.get();
            final UUID reservationId = item.getReservationId();

            reservationRestConsumer.delete(reservationId);

            LOGGER.debug("Deleted order item with product-id {} for order-id {}", orderId, productId);
            orderEventProducer.publish(orderId);

            return convert(order);
        } else {
            throw new ItemNotFoundException(orderId, productId);
        }
    }

    private Order getOrderByOrderId(final UUID orderId) {
        return orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    private OrderDto convert(final Order order) {
        return conversionService.convert(order, OrderDto.class);
    }
}
