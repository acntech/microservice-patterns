package no.acntech.order.service;

import no.acntech.order.exception.OrderItemAlreadyExistsException;
import no.acntech.order.exception.OrderItemNotFoundException;
import no.acntech.order.model.CreateOrderDto;
import no.acntech.order.model.CreateOrderItemDto;
import no.acntech.order.model.OrderDto;
import no.acntech.order.model.OrderEvent;
import no.acntech.order.model.OrderEventType;
import no.acntech.order.model.OrderItemDto;
import no.acntech.order.model.OrderQuery;
import no.acntech.order.model.UpdateOrderItemDto;
import no.acntech.order.producer.OrderEventProducer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("Duplicates")
@Service
public class OrderOrchestrationService {

    private final OrderService orderService;
    private final OrderEventProducer orderEventProducer;

    public OrderOrchestrationService(final OrderService orderService,
                                     final OrderEventProducer orderEventProducer) {
        this.orderService = orderService;
        this.orderEventProducer = orderEventProducer;
    }

    public List<OrderDto> findOrders(@NotNull final OrderQuery orderQuery) {
        return orderService.findOrders(orderQuery);
    }

    public OrderDto getOrder(@NotNull final UUID orderId) {
        return orderService.getOrder(orderId);
    }

    @Transactional
    public OrderDto createOrder(@Valid final CreateOrderDto createOrderDto) {
        var orderDto = orderService.createOrder(createOrderDto);
        var orderEvent = OrderEvent.builder()
                .eventType(OrderEventType.ORDER_CREATED)
                .orderId(orderDto.getOrderId())
                .orderStatus(orderDto.getStatus())
                .build();
        orderEventProducer.publish(orderEvent);
        return orderDto;
    }

    @Transactional
    public void updateOrder(@NotNull final UUID orderId) {
        final var updatedOrderDto = orderService.updateOrder(orderId);
        var orderEvent = OrderEvent.builder()
                .eventType(OrderEventType.ORDER_UPDATED)
                .orderId(updatedOrderDto.getOrderId())
                .orderStatus(updatedOrderDto.getStatus())
                .build();
        orderEventProducer.publish(orderEvent);
    }

    @Transactional
    public void deleteOrder(@NotNull final UUID orderId) {
        var orderDto = orderService.deleteOrder(orderId);
        var orderEvent = OrderEvent.builder()
                .eventType(OrderEventType.ORDER_CANCELED)
                .orderId(orderDto.getOrderId())
                .orderStatus(orderDto.getStatus())
                .build();
        orderEventProducer.publish(orderEvent);
    }

    public OrderItemDto getOrderItem(@NotNull final UUID itemId) {
        return orderService.getOrderItem(itemId);
    }

    @Transactional
    public OrderDto createOrderItem(@NotNull final UUID orderId,
                                    @NotNull @Valid final CreateOrderItemDto createOrderItemDto) {
        final var orderDto = orderService.getOrder(orderId);
        if (orderDto.hasItemWithProductId(createOrderItemDto.getProductId())) {
            throw new OrderItemAlreadyExistsException(orderId, createOrderItemDto.getProductId());
        }

        var updatedOrderDto = orderService.createOrderItem(orderId, createOrderItemDto);
        var updatedOrderItemDto = updatedOrderDto.getItems().stream()
                .filter(item -> item.getProductId() == createOrderItemDto.getProductId())
                .findFirst()
                .orElseThrow(() -> new OrderItemNotFoundException("No order item found for product-id " + createOrderItemDto.getProductId()));

        var orderEvent = OrderEvent.builder()
                .eventType(OrderEventType.ORDER_ITEM_ADDED)
                .orderId(updatedOrderDto.getOrderId())
                .orderStatus(updatedOrderDto.getStatus())
                .productId(updatedOrderItemDto.getProductId())
                .quantity(updatedOrderItemDto.getQuantity())
                .itemStatus(updatedOrderItemDto.getStatus())
                .build();
        orderEventProducer.publish(orderEvent);

        return updatedOrderDto;
    }

    @Transactional
    public void updateOrderItem(@NotNull final UUID itemId,
                                @NotNull @Valid final UpdateOrderItemDto updateOrderItemDto) {
        var updatedOrderDto = orderService.updateOrderItem(itemId, updateOrderItemDto);
        var updatedOrderItemDto = updatedOrderDto.getItems().stream()
                .filter(item -> item.getItemId() == itemId)
                .findFirst()
                .orElseThrow(() -> new OrderItemNotFoundException("No order item found for order-item-id " + itemId));

        var orderEvent = OrderEvent.builder()
                .eventType(OrderEventType.ORDER_ITEM_UPDATED)
                .orderId(updatedOrderDto.getOrderId())
                .orderStatus(updatedOrderDto.getStatus())
                .productId(updatedOrderItemDto.getProductId())
                .quantity(updatedOrderItemDto.getQuantity())
                .itemStatus(updatedOrderItemDto.getStatus())
                .build();
        orderEventProducer.publish(orderEvent);
    }

    @Transactional
    public void updateOrderItem(@NotNull @Valid final UpdateOrderItemDto updateOrderItemDto) {
        var updatedOrderDto = orderService.updateOrderItem(updateOrderItemDto);
        var updatedOrderItemDto = updatedOrderDto.getItems().stream()
                .filter(item -> item.getReservationId() == updateOrderItemDto.getReservationId())
                .findFirst()
                .orElseThrow(() -> new OrderItemNotFoundException("No order item found for reservation-id " + updateOrderItemDto.getReservationId()));

        var orderEvent = OrderEvent.builder()
                .eventType(OrderEventType.ORDER_ITEM_UPDATED)
                .orderId(updatedOrderDto.getOrderId())
                .orderStatus(updatedOrderDto.getStatus())
                .productId(updatedOrderItemDto.getProductId())
                .quantity(updatedOrderItemDto.getQuantity())
                .itemStatus(updatedOrderItemDto.getStatus())
                .build();
        orderEventProducer.publish(orderEvent);
    }

    @Transactional
    public void deleteOrderItem(@NotNull final UUID itemId) {
        var updatedOrderDto = orderService.deleteOrderItem(itemId);
        var updatedOrderItemDto = updatedOrderDto.getItems().stream()
                .filter(item -> item.getItemId() == itemId)
                .findFirst()
                .orElseThrow(() -> new OrderItemNotFoundException("No order item found for order-item-id " + itemId));

        var orderEvent = OrderEvent.builder()
                .eventType(OrderEventType.ORDER_ITEM_UPDATED)
                .orderId(updatedOrderDto.getOrderId())
                .orderStatus(updatedOrderDto.getStatus())
                .productId(updatedOrderItemDto.getProductId())
                .quantity(updatedOrderItemDto.getQuantity())
                .itemStatus(updatedOrderItemDto.getStatus())
                .build();
        orderEventProducer.publish(orderEvent);
    }
}
