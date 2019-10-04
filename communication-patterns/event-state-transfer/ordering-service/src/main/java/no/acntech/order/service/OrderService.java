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
            return orderRepository.findAllByCustomerIdAndStatus(customerId, status, SORT_BY_ID)
                    .stream()
                    .map(this::convertDto)
                    .collect(Collectors.toList());
        } else if (customerId != null) {
            return orderRepository.findAllByCustomerId(customerId, SORT_BY_ID)
                    .stream()
                    .map(this::convertDto)
                    .collect(Collectors.toList());
        } else if (status != null) {
            return orderRepository.findAllByStatus(status, SORT_BY_ID)
                    .stream()
                    .map(this::convertDto)
                    .collect(Collectors.toList());
        } else {
            return orderRepository.findAll(SORT_BY_ID)
                    .stream()
                    .map(this::convertDto)
                    .collect(Collectors.toList());
        }
    }

    public OrderDto getOrder(@NotNull final UUID orderId) {
        Order order = getOrderByOrderId(orderId);
        return convertDto(order);
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

        return convertDto(createdOrder);
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

        LOGGER.debug("Canceled order with order-id {}", canceledOrder.getOrderId());
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
        OrderDto orderDto = convertDto(order);
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

            LOGGER.debug("Added order item with product-id {} for order-id {}", orderId, productId);
            OrderEvent orderEvent = OrderEvent.builder()
                    .eventType(OrderEventType.ORDER_ITEM_ADDED)
                    .orderId(order.getOrderId())
                    .orderStatus(order.getStatus())
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .itemStatus(item.getStatus())
                    .build();
            orderEventProducer.publish(orderEvent);

            Order updatedOrder = getOrderByOrderId(orderId);
            return convertDto(updatedOrder);
        } else {
            throw new ItemAlreadyExistsException(orderId, productId);
        }
    }

    @Transactional
    public void updateItem(@NotNull final UUID itemId, @Valid final UpdateItemDto updateItem) {
        Long quantity = updateItem.getQuantity();

        Item item = getItemByItemId(itemId);
        Order order = getOrderById(item.getOrderId());

        UUID orderId = order.getOrderId();
        OrderStatus orderStatus = order.getStatus();
        UUID productId = item.getProductId();
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

        LOGGER.debug("Canceled order item with product-id {} for order-id {}", orderId, productId);
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

    private OrderDto convertDto(final Order order) {
        return conversionService.convert(order, OrderDto.class);
    }
}
