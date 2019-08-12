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
import no.acntech.order.model.CreateItemDto;
import no.acntech.order.model.CreateOrderDto;
import no.acntech.order.model.Item;
import no.acntech.order.model.ItemDto;
import no.acntech.order.model.ItemStatus;
import no.acntech.order.model.Order;
import no.acntech.order.model.OrderDto;
import no.acntech.order.model.OrderEvent;
import no.acntech.order.model.OrderEventType;
import no.acntech.order.model.OrderQuery;
import no.acntech.order.model.OrderStatus;
import no.acntech.order.model.UpdateItemDto;
import no.acntech.order.producer.OrderEventProducer;
import no.acntech.order.repository.ItemRepository;
import no.acntech.order.repository.OrderRepository;

@SuppressWarnings("Duplicates")
@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    private static final Sort SORT_BY_ID = Sort.by("id");

    private final ConversionService conversionService;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final OrderEventProducer orderEventProducer;

    public OrderService(final ConversionService conversionService,
                        final OrderRepository orderRepository,
                        final ItemRepository itemRepository,
                        final OrderEventProducer orderEventProducer) {
        this.conversionService = conversionService;
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.orderEventProducer = orderEventProducer;
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
        OrderEvent orderEvent = OrderEvent.builder()
                .eventType(OrderEventType.ORDER_CREATED)
                .orderId(createdOrder.getOrderId())
                .orderStatus(createdOrder.getStatus())
                .build();
        orderEventProducer.publish(orderEvent);

        return convert(createdOrder);
    }

    @Transactional
    public void updateOrder(@NotNull final UUID orderId) {
        Order order = getOrderByOrderId(orderId);

        order.confirmOrder();
        Order updatedOrder = orderRepository.save(order);

        LOGGER.debug("Updated order with order-id {}", updatedOrder.getOrderId());
        OrderEvent orderEvent = OrderEvent.builder()
                .eventType(OrderEventType.ORDER_UPDATED)
                .orderId(updatedOrder.getOrderId())
                .orderStatus(updatedOrder.getStatus())
                .build();
        orderEventProducer.publish(orderEvent);
    }

    @Transactional
    public void deleteOrder(@NotNull final UUID orderId) {
        Order order = getOrderByOrderId(orderId);

        order.cancelOrder();
        Order canceledOrder = orderRepository.save(order);

        LOGGER.debug("Deleted order with order-id {}", canceledOrder.getOrderId());
        OrderEvent orderEvent = OrderEvent.builder()
                .eventType(OrderEventType.ORDER_CANCELED)
                .orderId(canceledOrder.getOrderId())
                .orderStatus(canceledOrder.getStatus())
                .build();
        orderEventProducer.publish(orderEvent);
    }

    public ItemDto getItem(@NotNull final UUID itemId) {
        Item item = getItemByItemId(itemId);
        Order order = getOrderById(item.getOrderId());
        OrderDto orderDto = convert(order);
        return orderDto.getItems().stream()
                .filter(itemDto -> itemDto.getItemId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ItemNotFoundException(itemId));
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

            itemRepository.save(item);

            LOGGER.debug("Created order item with product-id {} for order-id {}", orderId, productId);
            OrderEvent orderEvent = OrderEvent.builder()
                    .eventType(OrderEventType.ORDER_ITEM_CREATED)
                    .orderId(order.getOrderId())
                    .orderStatus(order.getStatus())
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .itemStatus(item.getStatus())
                    .build();
            orderEventProducer.publish(orderEvent);

            Order updatedOrder = getOrderByOrderId(orderId);
            return convert(updatedOrder);
        } else {
            throw new ItemAlreadyExistsException(orderId, productId);
        }
    }

    @Transactional
    public void updateItem(@NotNull final UUID itemId, @Valid final UpdateItemDto updateItem) {
        Item item = getItemByItemId(itemId);
        Order order = getOrderById(item.getOrderId());

        UUID orderId = order.getOrderId();
        OrderStatus orderStatus = order.getStatus();
        UUID productId = item.getProductId();
        Long quantity = updateItem.getQuantity();
        ItemStatus itemStatus = item.getStatus();

        LOGGER.debug("Updated order item for order-id {} and product-id {}", orderId, productId);
        OrderEvent orderEvent = OrderEvent.builder()
                .eventType(OrderEventType.ORDER_ITEM_UPDATED)
                .orderId(orderId)
                .orderStatus(orderStatus)
                .productId(productId)
                .quantity(quantity)
                .itemStatus(itemStatus)
                .build();
        orderEventProducer.publish(orderEvent);
    }

    @Transactional
    public void deleteItem(@NotNull final UUID itemId) {
        Item item = getItemByItemId(itemId);
        Order order = getOrderById(item.getOrderId());

        UUID orderId = order.getOrderId();
        OrderStatus orderStatus = order.getStatus();
        UUID productId = item.getProductId();
        Long quantity = item.getQuantity();
        ItemStatus itemStatus = item.getStatus();

        LOGGER.debug("Deleted order item with product-id {} for order-id {}", orderId, productId);
        OrderEvent orderEvent = OrderEvent.builder()
                .eventType(OrderEventType.ORDER_ITEM_CANCELED)
                .orderId(orderId)
                .orderStatus(orderStatus)
                .productId(productId)
                .quantity(quantity)
                .itemStatus(itemStatus)
                .build();
        orderEventProducer.publish(orderEvent);
    }

    @Transactional
    public Optional<UUID> updateItemReservation(@NotNull UUID reservationId, @NotNull Long quantity, @NotNull ItemStatus status) {
        Optional<Item> exitingItem = itemRepository.findByReservationId(reservationId);

        if (exitingItem.isPresent()) {
            Item item = exitingItem.get();

            Optional<Order> existingOrder = orderRepository.findById(item.getOrderId());

            if (existingOrder.isPresent()) {
                Order order = existingOrder.get();
                UUID orderId = order.getOrderId();

                item.setStatus(status);
                item.setQuantity(quantity);

                LOGGER.debug("Updating order item status to {} for order-id {} and reservation-id {}", status.name(), orderId, reservationId);

                itemRepository.save(item);

                OrderStatus orderStatus = nextOrderStatus(order);

                if (OrderStatus.CONFIRMED.equals(orderStatus)) {
                    LOGGER.debug("Updating order status to {} for order-id {}", orderStatus.name(), orderId);
                    order.confirmOrder();
                    orderRepository.save(order);
                }

                return Optional.of(orderId);
            } else {
                LOGGER.error("Could not find order for reservation-id {}", reservationId);
                return Optional.empty();
            }
        } else {
            LOGGER.error("Could not find order item for reservation-id {}", reservationId);
            return Optional.empty();
        }
    }

    private Order getOrderById(final Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    private Order getOrderByOrderId(final UUID orderId) {
        return orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    private Item getItemByItemId(final UUID itemId) {
        return itemRepository.findByItemId(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));
    }

    private OrderStatus nextOrderStatus(final Order order) {
        if (areAllActiveItemsConfirmed(order)) {
            return OrderStatus.CONFIRMED;
        } else {
            return null;
        }
    }

    private boolean areAllActiveItemsConfirmed(final Order order) {
        List<Item> activeItems = order.getItems().stream()
                .filter(activeItem -> !ItemStatus.CANCELED.equals(activeItem.getStatus()))
                .collect(Collectors.toList());
        boolean allActiveItemsConfirmed = activeItems.stream()
                .map(Item::getStatus)
                .allMatch(ItemStatus.CONFIRMED::equals);
        return !activeItems.isEmpty() && allActiveItemsConfirmed;
    }

    private OrderDto convert(final Order order) {
        return conversionService.convert(order, OrderDto.class);
    }
}
