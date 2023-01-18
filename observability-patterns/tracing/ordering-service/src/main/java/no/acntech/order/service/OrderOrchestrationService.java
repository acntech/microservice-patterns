package no.acntech.order.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.acntech.order.exception.OrderItemAlreadyExistsException;
import no.acntech.order.model.CreateOrderDto;
import no.acntech.order.model.CreateOrderItemDto;
import no.acntech.order.model.OrderDto;
import no.acntech.order.model.OrderItemDto;
import no.acntech.order.model.OrderItemStatus;
import no.acntech.order.model.OrderQuery;
import no.acntech.order.model.UpdateOrderItemDto;
import no.acntech.reservation.consumer.ReservationRestConsumer;
import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.UpdateReservationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Validated
@Service
public class OrderOrchestrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderOrchestrationService.class);
    private final ConversionService conversionService;
    private final OrderService orderService;
    private final ReservationRestConsumer reservationRestConsumer;

    public OrderOrchestrationService(final ConversionService conversionService,
                                     final OrderService orderService,
                                     final ReservationRestConsumer reservationRestConsumer) {
        this.conversionService = conversionService;
        this.orderService = orderService;
        this.reservationRestConsumer = reservationRestConsumer;
    }

    public OrderDto getOrder(@NotNull final UUID orderId) {
        LOGGER.debug("Getting order for ID {}", orderId);
        return orderService.getOrder(orderId);
    }

    public List<OrderDto> findOrders(@NotNull @Valid final OrderQuery orderQuery) {
        LOGGER.debug("Finding orders");
        return orderService.findOrders(orderQuery);
    }

    @Transactional
    public OrderDto createOrder(@Valid final CreateOrderDto createOrder) {
        LOGGER.debug("Create order");
        return orderService.createOrder(createOrder);
    }

    @Transactional
    public OrderDto updateOrder(@NotNull final UUID orderId) {
        LOGGER.debug("Update order for ID {}", orderId);
        final var orderDto = orderService.updateOrder(orderId);
        orderDto.getItems()
                .forEach(item -> {
                    final var updateReservationDto = UpdateReservationDto.builder()
                            .statusConfirmed()
                            .build();
                    LOGGER.debug("Updating reservation status to {} for reservation-id {}", updateReservationDto.getStatus().name(), item.getReservationId());
                    reservationRestConsumer.update(item.getReservationId(), updateReservationDto);
                });
        return orderDto;
    }

    @Transactional
    public OrderDto deleteOrder(@NotNull final UUID orderId) {
        LOGGER.debug("Delete order for ID {}", orderId);
        final var orderDto = orderService.deleteOrder(orderId);
        orderDto.getItems().stream()
                .map(OrderItemDto::getReservationId)
                .forEach(reservationRestConsumer::delete);
        return orderDto;
    }

    public OrderItemDto getOrderItem(@NotNull final UUID itemId) {
        LOGGER.debug("Getting order item for ID {}", itemId);
        return orderService.getOrderItem(itemId);
    }

    @Transactional
    public OrderDto createOrderItem(@NotNull final UUID orderId,
                                    @NotNull @Valid final CreateOrderItemDto createOrderItemDto) {
        LOGGER.debug("Create order item for order with ID {}", orderId);
        final var orderDto = orderService.getOrder(orderId);
        if (orderDto.hasItemWithProductId(createOrderItemDto.getProductId())) {
            throw new OrderItemAlreadyExistsException(orderId, createOrderItemDto.getProductId());
        }
        final var createReservationDto = CreateReservationDto.builder()
                .orderId(orderId)
                .productId(createOrderItemDto.getProductId())
                .quantity(createOrderItemDto.getQuantity())
                .build();
        final var reservationDto = reservationRestConsumer.create(createReservationDto);
        createOrderItemDto.setReservationId(reservationDto.getReservationId());
        createOrderItemDto.setStatus(OrderItemStatus.valueOf(reservationDto.getStatus().name()));
        return orderService.createOrderItem(orderId, createOrderItemDto);
    }

    @Transactional
    public OrderDto updateOrderItem(@NotNull final UUID itemId,
                                    @NotNull @Valid final UpdateOrderItemDto updateOrderItemDto) {
        LOGGER.debug("Update order item for ID {}", itemId);
        final var orderItemDto = orderService.getOrderItem(itemId);
        final var updateReservationDto = conversionService.convert(updateOrderItemDto, UpdateReservationDto.class);
        Assert.notNull(updateReservationDto, "Failed to convert UpdateOrderItemDto to UpdateReservationDto");
        final var reservationDto = reservationRestConsumer.update(orderItemDto.getReservationId(), updateReservationDto);
        final var reservedUpdateOrderItemDto = conversionService.convert(reservationDto, UpdateOrderItemDto.class);
        Assert.notNull(reservedUpdateOrderItemDto, "Failed to convert ReservationDto to UpdateOrderItemDto");
        final var orderDto = orderService.updateOrderItem(itemId, reservedUpdateOrderItemDto);
        if (orderDto.areAllItemsConfirmed()) {
            return orderService.updateOrder(orderDto.getOrderId());
        } else {
            return orderDto;
        }
    }

    @Transactional
    public OrderDto deleteOrderItem(@NotNull final UUID itemId) {
        LOGGER.debug("Delete order item for ID {}", itemId);
        final var orderItemDto = orderService.getOrderItem(itemId);
        final var reservationDto = reservationRestConsumer.delete(orderItemDto.getReservationId());
        LOGGER.debug("Deleted order item for item-id {}", itemId);
        final var updateOrderItemDto = conversionService.convert(reservationDto, UpdateOrderItemDto.class);
        Assert.notNull(updateOrderItemDto, "Failed to convert ReservationDto to UpdateOrderItemDto");
        return orderService.updateOrderItem(itemId, updateOrderItemDto);
    }
}
