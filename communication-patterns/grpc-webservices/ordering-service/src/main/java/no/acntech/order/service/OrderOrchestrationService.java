package no.acntech.order.service;

import com.google.protobuf.StringValue;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.acntech.invoice.consumer.InvoiceConsumer;
import no.acntech.invoice.model.CreateInvoiceDto;
import no.acntech.order.exception.OrderItemAlreadyExistsException;
import no.acntech.order.model.*;
import no.acntech.reservation.consumer.ReservationConsumer;
import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.ReservationStatus;
import no.acntech.reservation.model.UpdateReservationDto;
import no.acntech.shipment.consumer.ShipmentConsumer;
import no.acntech.shipment.model.CreateShipmentDto;
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
    private final ReservationConsumer reservationConsumer;
    private final InvoiceConsumer invoiceConsumer;
    private final ShipmentConsumer shipmentConsumer;

    public OrderOrchestrationService(final ConversionService conversionService,
                                     final OrderService orderService,
                                     final ReservationConsumer reservationConsumer,
                                     final InvoiceConsumer invoiceConsumer,
                                     final ShipmentConsumer shipmentConsumer) {
        this.conversionService = conversionService;
        this.orderService = orderService;
        this.reservationConsumer = reservationConsumer;
        this.invoiceConsumer = invoiceConsumer;
        this.shipmentConsumer = shipmentConsumer;
    }

    public OrderDto getOrder(@NotNull final UUID orderId) {
        LOGGER.debug("Get order {}", orderId);
        return orderService.getOrder(orderId);
    }

    public List<OrderDto> findOrders(@NotNull @Valid final OrderQuery orderQuery) {
        LOGGER.debug("Find orders");
        return orderService.findOrders(orderQuery);
    }

    @Transactional
    public OrderDto createOrder(@Valid final CreateOrderDto createOrder) {
        LOGGER.debug("Create order");
        return orderService.createOrder(createOrder);
    }

    @Transactional
    public OrderDto updateOrder(@NotNull final UUID orderId) {
        LOGGER.debug("Update order {}", orderId);
        final var orderDto = orderService.updateOrder(orderId);
        orderDto.getItemsList().stream()
                .filter(item -> item.getStatus() == OrderItemStatus.RESERVED)
                .forEach(item -> {
                    final var updateReservationDto = UpdateReservationDto.newBuilder()
                            .setStatus(ReservationStatus.CONFIRMED)
                            .build();
                    LOGGER.debug("Updating reservation status to {} for reservation-id {}", updateReservationDto.getStatus().name(), item.getReservationId());
                    reservationConsumer.updateReservation(UUID.fromString(item.getReservationId().getValue()), updateReservationDto);
                });
        return orderDto;
    }

    @Transactional
    public OrderDto deleteOrder(@NotNull final UUID orderId) {
        LOGGER.debug("Delete order {}", orderId);
        final var orderDto = orderService.deleteOrder(orderId);
        orderDto.getItemsList().stream()
                .map(OrderItemDto::getReservationId)
                .map(StringValue::getValue)
                .map(UUID::fromString)
                .forEach(reservationConsumer::deleteReservation);
        return orderDto;
    }

    public OrderItemDto getOrderItem(@NotNull final UUID orderItemId) {
        LOGGER.debug("Get order item {}", orderItemId);
        return orderService.getOrderItem(orderItemId);
    }

    public List<OrderItemDto> findOrderItems(@NotNull final OrderItemQuery query) {
        return orderService.findOrderItems(query);
    }

    @Transactional
    public OrderDto createOrderItem(@NotNull final UUID orderId,
                                    @NotNull @Valid final CreateOrderItemDto createOrderItemDto) {
        LOGGER.debug("Create order item for order {}", orderId);
        final var orderDto = orderService.getOrder(orderId);
        if (orderDto.getItemsList().stream()
                .map(OrderItemDto::getProductId)
                .map(StringValue::getValue)
                .anyMatch(productId -> productId.equals(createOrderItemDto.getProductId().getValue()))) {
            throw new OrderItemAlreadyExistsException(orderId, UUID.fromString(createOrderItemDto.getProductId().getValue()));
        }
        final var createReservationDto = CreateReservationDto.newBuilder()
                .setOrderId(StringValue.of(orderId.toString()))
                .setProductId(createOrderItemDto.getProductId())
                .setQuantity(createOrderItemDto.getQuantity())
                .build();
        final var reservationDto = reservationConsumer.createReservation(createReservationDto);
        final var modifiedCreateOrderItemDto = CreateOrderItemDto.newBuilder(createOrderItemDto)
                .setReservationId(reservationDto.getReservationId())
                .setStatus(OrderItemStatus.valueOf(reservationDto.getStatus().name()))
                .build();
        return orderService.createOrderItem(orderId, modifiedCreateOrderItemDto);
    }

    @Transactional
    public OrderDto updateOrderItem(@NotNull final UUID orderItemId,
                                    @NotNull @Valid final UpdateOrderItemDto updateOrderItemDto) {
        LOGGER.debug("Update order item {}", orderItemId);
        final var orderItemDto = orderService.getOrderItem(orderItemId);
        final var updateReservationDto = conversionService.convert(updateOrderItemDto, UpdateReservationDto.class);
        Assert.notNull(updateReservationDto, "Failed to convert UpdateOrderItemDto to UpdateReservationDto");
        final var reservationDto = reservationConsumer.updateReservation(
                UUID.fromString(orderItemDto.getReservationId().getValue()),
                updateReservationDto);
        final var reservedUpdateOrderItemDto = conversionService.convert(reservationDto, UpdateOrderItemDto.class);
        Assert.notNull(reservedUpdateOrderItemDto, "Failed to convert ReservationDto to UpdateOrderItemDto");
        final var orderDto = orderService.updateOrderItem(orderItemId, reservedUpdateOrderItemDto);
        if (orderDto.getItemsList().stream()
                .map(OrderItemDto::getStatus)
                .allMatch(status -> status == OrderItemStatus.CONFIRMED)) {
            final var updatedOrderDto = orderService.updateOrder(UUID.fromString(orderDto.getOrderId().getValue()));
            final var createInvoiceDto = conversionService.convert(updatedOrderDto, CreateInvoiceDto.class);
            Assert.notNull(createInvoiceDto, "Failed to convert OrderDto to CreateInvoiceDto");
            invoiceConsumer.createInvoice(createInvoiceDto);
            final var createShipmentDto = conversionService.convert(updatedOrderDto, CreateShipmentDto.class);
            Assert.notNull(createShipmentDto, "Failed to convert OrderDto to CreateShipmentDto");
            shipmentConsumer.createShipment(createShipmentDto);
            return updatedOrderDto;
        } else {
            return orderDto;
        }
    }

    @Transactional
    public OrderDto deleteOrderItem(@NotNull final UUID orderItemId) {
        LOGGER.debug("Delete order item {}", orderItemId);
        final var orderItemDto = orderService.getOrderItem(orderItemId);
        final var reservationDto = reservationConsumer.deleteReservation(UUID.fromString(orderItemDto.getReservationId().getValue()));
        LOGGER.debug("Deleted order item for item-id {}", orderItemId);
        final var updateOrderItemDto = conversionService.convert(reservationDto, UpdateOrderItemDto.class);
        Assert.notNull(updateOrderItemDto, "Failed to convert ReservationDto to UpdateOrderItemDto");
        return orderService.updateOrderItem(orderItemId, updateOrderItemDto);
    }
}
