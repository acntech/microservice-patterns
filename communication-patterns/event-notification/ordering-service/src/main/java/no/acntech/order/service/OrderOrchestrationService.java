package no.acntech.order.service;

import no.acntech.order.exception.OrderItemAlreadyExistsException;
import no.acntech.order.exception.ReservationNotReceivedException;
import no.acntech.order.producer.OrderEventProducer;
import no.acntech.reservation.consumer.ReservationRestConsumer;
import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.UpdateReservationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Validated
@Service
public class OrderOrchestrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderOrchestrationService.class);
    private final OrderService orderService;
    private final OrderEventProducer orderEventProducer;
    private final ReservationRestConsumer reservationRestConsumer;

    public OrderOrchestrationService(final OrderService orderService,
                                     final OrderEventProducer orderEventProducer,
                                     final ReservationRestConsumer reservationRestConsumer) {
        this.orderService = orderService;
        this.orderEventProducer = orderEventProducer;
        this.reservationRestConsumer = reservationRestConsumer;
    }

    public List<OrderDto> findOrders(@NotNull @Valid final OrderQuery orderQuery) {
        return orderService.findOrders(orderQuery);
    }

    public OrderDto getOrder(@NotNull final UUID orderId) {
        return orderService.getOrder(orderId);
    }

    @Transactional
    public OrderDto createOrder(@Valid final CreateOrderDto createOrder) {
        final var orderDto = orderService.createOrder(createOrder);
        orderEventProducer.publish(orderDto.getOrderId());
        return orderDto;
    }

    @Transactional
    public void updateOrder(@NotNull final UUID orderId) {
        final var orderDto = orderService.getOrder(orderId);
        orderDto.getItems().stream()
                .map(OrderItemDto::getReservationId)
                .forEach(reservationId -> {
                    final var updateReservationDto = UpdateReservationDto.builder()
                            .statusConfirmed()
                            .build();
                    LOGGER.debug("Updating reservation status to {} for reservation-id {}", updateReservationDto.getStatus().name(), reservationId);
                    reservationRestConsumer.update(reservationId, updateReservationDto);
                });
    }

    @Transactional
    public void deleteOrder(@NotNull final UUID orderId) {
        final var orderDto = orderService.deleteOrder(orderId);
        orderDto.getItems().stream()
                .map(OrderItemDto::getReservationId)
                .forEach(reservationRestConsumer::delete);
        orderEventProducer.publish(orderId);
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
        final var createReservationDto = CreateReservationDto.builder()
                .orderId(orderId)
                .productId(createOrderItemDto.getProductId())
                .quantity(createOrderItemDto.getQuantity())
                .build();
        final var pendingReservationDto = reservationRestConsumer.create(createReservationDto)
                .orElseThrow(() -> new ReservationNotReceivedException(orderId, createOrderItemDto.getProductId()));
        createOrderItemDto.setReservationId(pendingReservationDto.getReservationId());
        final var updatedOrderDto = orderService.createOrderItem(orderId, createOrderItemDto);
        orderEventProducer.publish(orderId);
        return updatedOrderDto;
    }

    @Transactional
    public void updateOrderItem(@NotNull final UUID itemId,
                                @NotNull @Valid final UpdateOrderItemDto updateOrderItemDto) {
        final var orderItemDto = orderService.getOrderItem(itemId);
        final var updateReservationDto = UpdateReservationDto.builder()
                .quantity(updateOrderItemDto.getQuantity())
                .build();
        reservationRestConsumer.update(orderItemDto.getReservationId(), updateReservationDto);
        orderEventProducer.publish(orderItemDto.getOrderId());
    }

    @Transactional
    public void deleteOrderItem(@NotNull final UUID itemId) {
        final var orderItemDto = orderService.getOrderItem(itemId);
        reservationRestConsumer.delete(orderItemDto.getReservationId());
        LOGGER.debug("Deleted order item for item-id {}", itemId);
        orderEventProducer.publish(orderItemDto.getOrderId());
    }

    @Transactional
    public void updateOrderItemReservation(@NotNull @Valid final UpdateOrderItemDto updateOrderItemDto) {
        final var orderDto = orderService.updateOrderItem(updateOrderItemDto);
        orderEventProducer.publish(orderDto.getOrderId());
    }
}
