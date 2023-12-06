package no.acntech.order.service;

import no.acntech.order.exception.OrderItemNotFoundException;
import no.acntech.order.exception.OrderNotFoundException;
import no.acntech.order.model.CreateOrderDto;
import no.acntech.order.model.CreateOrderItemDto;
import no.acntech.order.model.OrderDto;
import no.acntech.order.model.OrderEntity;
import no.acntech.order.model.OrderItemDto;
import no.acntech.order.model.OrderItemEntity;
import no.acntech.order.model.OrderQuery;
import no.acntech.order.model.UpdateOrderItemDto;
import no.acntech.order.repository.OrderItemRepository;
import no.acntech.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
@Validated
@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    private static final Sort SORT_BY_ID = Sort.by("id");
    private final ConversionService conversionService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(final ConversionService conversionService,
                        final OrderRepository orderRepository,
                        final OrderItemRepository orderItemRepository) {
        this.conversionService = conversionService;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public List<OrderDto> findOrders(@NotNull @Valid final OrderQuery orderQuery) {
        if (orderQuery.getCustomerId() != null && orderQuery.getStatus() != null) {
            return orderRepository.findAllByCustomerIdAndStatus(orderQuery.getCustomerId(), orderQuery.getStatus(), SORT_BY_ID)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else if (orderQuery.getCustomerId() != null) {
            return orderRepository.findAllByCustomerId(orderQuery.getCustomerId(), SORT_BY_ID)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else if (orderQuery.getStatus() != null) {
            return orderRepository.findAllByStatus(orderQuery.getStatus(), SORT_BY_ID)
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
        return orderRepository.findByOrderId(orderId)
                .map(this::convert)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Transactional
    public OrderDto createOrder(@Valid final CreateOrderDto createOrder) {
        final var orderEntity = conversionService.convert(createOrder, OrderEntity.class);
        Assert.notNull(orderEntity, "Failed to convert CreateOrderDto to OrderEntity");
        final var createdOrderEntity = orderRepository.save(orderEntity);
        LOGGER.debug("Created order with order-id {}", createdOrderEntity.getOrderId());
        return convert(createdOrderEntity);
    }

    @Transactional
    public OrderDto updateOrder(@Valid final UUID orderId) {
        var orderEntity = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        orderEntity.confirmOrder();
        final var updatedOrderEntity = orderRepository.save(orderEntity);
        LOGGER.debug("Updated order with order-id {}", updatedOrderEntity.getOrderId());
        return convert(updatedOrderEntity);
    }

    @Transactional
    public OrderDto deleteOrder(@NotNull final UUID orderId) {
        final var orderEntity = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        orderEntity.cancelOrder();
        final var deletedOrderEntity = orderRepository.save(orderEntity);
        LOGGER.debug("Deleted order with order-id {}", orderId);
        return convert(deletedOrderEntity);
    }

    public OrderItemDto getOrderItem(@NotNull final UUID itemId) {
        return orderItemRepository.findByItemId(itemId)
                .map(this::convert)
                .orElseThrow(() -> new OrderItemNotFoundException("No order item found for item-id " + itemId));
    }

    @Transactional
    public OrderDto createOrderItem(@NotNull final UUID orderId,
                                    @NotNull @Valid final CreateOrderItemDto createOrderItemDto) {
        final var orderEntity = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        final var orderItemEntity = OrderItemEntity.builder()
                .productId(createOrderItemDto.getProductId())
                .reservationId(createOrderItemDto.getReservationId())
                .quantity(createOrderItemDto.getQuantity())
                .build();
        orderEntity.addItem(orderItemEntity);
        final var updatedOrderEntity = orderRepository.save(orderEntity);

        LOGGER.debug("Created order item with product-id {} for order-id {}", orderId, createOrderItemDto.getProductId());
        return convert(updatedOrderEntity);
    }

    @Transactional
    public OrderDto updateOrderItem(@NotNull final UUID itemId,
                                    @NotNull @Valid final UpdateOrderItemDto updateOrderItemDto) {
        final var orderItemEntity = orderItemRepository.findByItemId(itemId)
                .orElseThrow(() -> new OrderItemNotFoundException("No order item found for order-item-id " + itemId));

        return updateOrderItem(orderItemEntity, updateOrderItemDto);
    }

    @Transactional
    public OrderDto updateOrderItem(@NotNull @Valid final UpdateOrderItemDto updateOrderItemDto) {
        final var orderItemEntity = orderItemRepository.findByReservationId(updateOrderItemDto.getReservationId())
                .orElseThrow(() -> new OrderItemNotFoundException("No order item found for reservation-id " + updateOrderItemDto.getReservationId()));

        return updateOrderItem(orderItemEntity, updateOrderItemDto);
    }

    private OrderDto updateOrderItem(@NotNull final OrderItemEntity orderItemEntity,
                                     @NotNull @Valid final UpdateOrderItemDto updateOrderItemDto) {
        orderItemEntity.setQuantity(updateOrderItemDto.getQuantity());
        if (updateOrderItemDto.getStatus() != null) {
            orderItemEntity.setStatus(updateOrderItemDto.getStatus());
        }

        LOGGER.debug("Updating order item for order-id {} and order-item-id {}", orderItemEntity.getParent().getOrderId(), orderItemEntity.getItemId());

        orderItemRepository.save(orderItemEntity);

        var orderEntity = orderRepository.findByOrderId(orderItemEntity.getParent().getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(orderItemEntity.getParent().getOrderId()));

        if (orderEntity.areAllActiveItemsConfirmed()) {
            orderEntity.confirmOrder();
            final var savedOrderEntity = orderRepository.save(orderEntity);
            LOGGER.debug("Updated order status to {} for order-id {}", savedOrderEntity.getStatus().name(), orderEntity.getOrderId());
            return convert(savedOrderEntity);
        } else {
            return convert(orderEntity);
        }
    }

    @Transactional
    public OrderDto deleteOrderItem(@NotNull final UUID itemId) {
        final var orderItemEntity = orderItemRepository.findByItemId(itemId)
                .orElseThrow(() -> new OrderItemNotFoundException("No order item found for order-item-id " + itemId));
        orderItemEntity.cancelOrderItem();

        LOGGER.debug("Deleting order item for order-id {} and order-item-id {}", orderItemEntity.getParent().getOrderId(), orderItemEntity.getItemId());

        orderItemRepository.save(orderItemEntity);

        var orderEntity = orderRepository.findByOrderId(orderItemEntity.getParent().getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(orderItemEntity.getParent().getOrderId()));

        if (orderEntity.areAllActiveItemsConfirmed()) {
            orderEntity.confirmOrder();
            final var savedOrderEntity = orderRepository.save(orderEntity);
            LOGGER.debug("Updated order status to {} for order-id {}", savedOrderEntity.getStatus().name(), orderEntity.getOrderId());
            return convert(savedOrderEntity);
        } else {
            return convert(orderEntity);
        }
    }

    private OrderDto convert(final OrderEntity order) {
        final var orderDto = conversionService.convert(order, OrderDto.class);
        Assert.notNull(order, "Failed to convert OrderEntity to OrderDto");
        return orderDto;
    }

    private OrderItemDto convert(final OrderItemEntity order) {
        final var orderDto = conversionService.convert(order, OrderItemDto.class);
        Assert.notNull(order, "Failed to convert OrderItemEntity to OrderItemDto");
        return orderDto;
    }
}
