package no.acntech.order.service;

import no.acntech.invoice.model.CreateInvoiceDto;
import no.acntech.invoice.producer.InvoiceRabbitProducer;
import no.acntech.order.exception.OrderItemAlreadyExistsException;
import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.DeleteReservationDto;
import no.acntech.reservation.model.UpdateReservationDto;
import no.acntech.reservation.producer.ReservationRabbitProducer;
import no.acntech.shipment.model.CreateShipmentDto;
import no.acntech.shipment.producer.ShipmentRabbitProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Validated
@Service
public class OrderOrchestrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderOrchestrationService.class);
    private final ConversionService conversionService;
    private final OrderService orderService;
    private final ReservationRabbitProducer reservationRabbitProducer;
    private final InvoiceRabbitProducer invoiceRabbitProducer;
    private final ShipmentRabbitProducer shipmentRabbitProducer;

    public OrderOrchestrationService(final ConversionService conversionService,
                                     final OrderService orderService,
                                     final ReservationRabbitProducer reservationRabbitProducer,
                                     final InvoiceRabbitProducer invoiceRabbitProducer,
                                     final ShipmentRabbitProducer shipmentRabbitProducer) {
        this.conversionService = conversionService;
        this.orderService = orderService;
        this.reservationRabbitProducer = reservationRabbitProducer;
        this.invoiceRabbitProducer = invoiceRabbitProducer;
        this.shipmentRabbitProducer = shipmentRabbitProducer;
    }

    public List<OrderDto> findOrders(@NotNull @Valid final OrderQuery orderQuery) {
        return orderService.findOrders(orderQuery);
    }

    public OrderDto getOrder(@NotNull final UUID orderId) {
        return orderService.getOrder(orderId);
    }

    @Transactional
    public OrderDto createOrder(@Valid final CreateOrderDto createOrder) {
        return orderService.createOrder(createOrder);
    }

    @Transactional
    public OrderDto updateOrder(@NotNull final UUID orderId) {
        final var orderDto = orderService.updateOrder(orderId);
        orderDto.getItems()
                .forEach(item -> {
                    final var updateReservationDto = UpdateReservationDto.builder()
                            .reservationId(item.getReservationId())
                            .statusConfirmed()
                            .build();
                    LOGGER.debug("Updating reservation status to {} for reservation-id {}", updateReservationDto.getStatus().name(), updateReservationDto.getReservationId());
                    reservationRabbitProducer.update(updateReservationDto);
                });
        return orderDto;
    }

    @Transactional
    public OrderDto deleteOrder(@NotNull final UUID orderId) {
        final var orderDto = orderService.deleteOrder(orderId);
        orderDto.getItems().stream()
                .map(OrderItemDto::getReservationId)
                .map(reservationId -> DeleteReservationDto.builder()
                        .reservationId(reservationId)
                        .build())
                .forEach(reservationRabbitProducer::delete);
        return orderDto;
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
        reservationRabbitProducer.create(createReservationDto);
        return orderService.createOrderItem(orderId, createOrderItemDto);
    }

    @Transactional
    public OrderDto updateOrderItem(@NotNull final UUID itemId,
                                    @NotNull @Valid final UpdateOrderItemDto updateOrderItemDto) {
        final var orderDto = orderService.updateOrderItem(itemId, updateOrderItemDto);
        final var updateReservationDto = conversionService.convert(updateOrderItemDto, UpdateReservationDto.class);
        Assert.notNull(updateReservationDto, "Failed to convert UpdateOrderItemDto to UpdateReservationDto");
        reservationRabbitProducer.update(updateReservationDto);

        if (orderDto.areAllItemsConfirmed()) {
            final var updatedOrderDto = orderService.updateOrder(orderDto.getOrderId());
            final var createInvoiceDto = conversionService.convert(updatedOrderDto, CreateInvoiceDto.class);
            Assert.notNull(createInvoiceDto, "Failed to convert OrderDto to CreateInvoiceDto");
            invoiceRabbitProducer.create(createInvoiceDto);
            final var createShipmentDto = conversionService.convert(updatedOrderDto, CreateShipmentDto.class);
            Assert.notNull(createShipmentDto, "Failed to convert OrderDto to CreateShipmentDto");
            shipmentRabbitProducer.create(createShipmentDto);
            return updatedOrderDto;
        } else {
            return orderDto;
        }
    }

    @Transactional
    public OrderDto deleteOrderItem(@NotNull final UUID itemId) {
        final var orderItemDto = orderService.getOrderItem(itemId);
        final var deleteReservationDto = conversionService.convert(orderItemDto, DeleteReservationDto.class);
        Assert.notNull(deleteReservationDto, "Failed to convert OrderItemDto to DeleteReservationDto");
        reservationRabbitProducer.delete(deleteReservationDto);
        LOGGER.debug("Deleted order item for item-id {}", itemId);
        return orderService.updateOrderItem(itemId, UpdateOrderItemDto.builder()
                .status(OrderItemStatus.CANCELED)
                .build());
    }
}
