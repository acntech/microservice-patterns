package no.acntech.order.service;

import no.acntech.order.model.OrderEvent;
import no.acntech.reservation.model.CancelReservationDto;
import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.UpdateReservationDto;
import no.acntech.reservation.service.ReservationOrchestrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final ConversionService conversionService;
    private final ReservationOrchestrationService reservationOrchestrationService;

    public OrderService(final ConversionService conversionService,
                        final ReservationOrchestrationService reservationOrchestrationService) {
        this.conversionService = conversionService;
        this.reservationOrchestrationService = reservationOrchestrationService;
    }

    public void processOrderEvent(final OrderEvent orderEvent) {
        LOGGER.debug("Processing order event for order-id {}", orderEvent.getOrderId());

        try {
            switch (orderEvent.getEventType()) {
                case ORDER_UPDATED -> processOrderUpdated(orderEvent);
                case ORDER_CANCELED -> processOrderCanceled(orderEvent);
                case ORDER_ITEM_ADDED -> processOrderItemAdded(orderEvent);
                case ORDER_ITEM_UPDATED -> processOrderItemUpdated(orderEvent);
                case ORDER_ITEM_CANCELED -> processOrderItemCanceled(orderEvent);
                default ->
                        LOGGER.debug("Ignoring order event with type {} for order-id {}", orderEvent.getEventType(), orderEvent.getOrderId());
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while processing order event", e);
        }
    }

    private void processOrderUpdated(final OrderEvent orderEvent) {
        final var updateReservationDto = conversionService.convert(orderEvent, UpdateReservationDto.class);
        Assert.notNull(updateReservationDto, "Failed to convert OrderEvent to UpdateReservationDto");
        reservationOrchestrationService.updateAllReservations(updateReservationDto);
    }

    private void processOrderCanceled(final OrderEvent orderEvent) {
        final var cancelReservationDto = conversionService.convert(orderEvent, CancelReservationDto.class);
        Assert.notNull(cancelReservationDto, "Failed to convert OrderEvent to CancelReservationDto");
        reservationOrchestrationService.cancelAllReservations(cancelReservationDto);
    }

    private void processOrderItemAdded(final OrderEvent orderEvent) {
        final var createReservationDto = conversionService.convert(orderEvent, CreateReservationDto.class);
        Assert.notNull(createReservationDto, "Failed to convert OrderEvent to CreateReservationDto");
        reservationOrchestrationService.createReservation(createReservationDto);
    }

    private void processOrderItemUpdated(final OrderEvent orderEvent) {
        final var updateReservationDto = conversionService.convert(orderEvent, UpdateReservationDto.class);
        Assert.notNull(updateReservationDto, "Failed to convert OrderEvent to UpdateReservationDto");
        reservationOrchestrationService.updateReservation(updateReservationDto);
    }

    private void processOrderItemCanceled(final OrderEvent orderEvent) {
        final var cancelReservationDto = conversionService.convert(orderEvent, CancelReservationDto.class);
        Assert.notNull(cancelReservationDto, "Failed to convert OrderEvent to CancelReservationDto");
        reservationOrchestrationService.cancelReservation(cancelReservationDto);
    }
}
