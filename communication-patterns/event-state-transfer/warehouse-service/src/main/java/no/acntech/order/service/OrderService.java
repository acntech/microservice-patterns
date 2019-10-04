package no.acntech.order.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import no.acntech.order.model.OrderEvent;
import no.acntech.order.model.OrderEventType;
import no.acntech.reservation.model.CancelReservationDto;
import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.UpdateReservationDto;
import no.acntech.reservation.service.ReservationService;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final ConversionService conversionService;
    private final ReservationService reservationService;

    public OrderService(final ConversionService conversionService,
                        final ReservationService reservationService) {
        this.conversionService = conversionService;
        this.reservationService = reservationService;
    }

    public void receiveOrderEvent(final OrderEvent orderEvent) {
        try {
            processReservationEvent(orderEvent);
        } catch (Exception e) {
            LOGGER.error("Error occurred while processing order event", e);
        }
    }

    private void processReservationEvent(final OrderEvent orderEvent) {
        OrderEventType eventType = orderEvent.getEventType();
        UUID orderId = orderEvent.getOrderId();

        LOGGER.debug("Processing order event for order-id {}", orderId);

        switch (eventType) {
            case ORDER_UPDATED:
                processOrderUpdated(orderEvent);
                break;
            case ORDER_CANCELED:
                processOrderCanceled(orderEvent);
                break;
            case ORDER_ITEM_ADDED:
                processOrderItemAdded(orderEvent);
                break;
            case ORDER_ITEM_UPDATED:
                processOrderItemUpdated(orderEvent);
                break;
            case ORDER_ITEM_CANCELED:
                processOrderItemCanceled(orderEvent);
                break;
            default:
                LOGGER.debug("Ignoring order event with type {} for order-id {}", eventType, orderId);
        }
    }

    private void processOrderUpdated(final OrderEvent orderEvent) {
        UpdateReservationDto updateReservation = conversionService.convert(orderEvent, UpdateReservationDto.class);
        if (updateReservation != null) {
            reservationService.updateAllReservations(updateReservation);
        } else {
            LOGGER.error("Could not convert order event to update reservation DTO");
        }
    }

    private void processOrderCanceled(final OrderEvent orderEvent) {
        CancelReservationDto cancelReservation = conversionService.convert(orderEvent, CancelReservationDto.class);
        if (cancelReservation != null) {
            reservationService.cancelAllReservations(cancelReservation);
        } else {
            LOGGER.error("Could not convert order event to cancel reservation DTO");
        }
    }

    private void processOrderItemAdded(final OrderEvent orderEvent) {
        CreateReservationDto createReservation = conversionService.convert(orderEvent, CreateReservationDto.class);
        if (createReservation != null) {
            reservationService.createReservation(createReservation);
        } else {
            LOGGER.error("Could not convert order event to create reservation DTO");
        }
    }

    private void processOrderItemUpdated(final OrderEvent orderEvent) {
        UpdateReservationDto updateReservation = conversionService.convert(orderEvent, UpdateReservationDto.class);
        if (updateReservation != null) {
            reservationService.updateReservation(updateReservation);
        } else {
            LOGGER.error("Could not convert order event to update reservation DTO");
        }
    }

    private void processOrderItemCanceled(final OrderEvent orderEvent) {
        CancelReservationDto cancelReservation = conversionService.convert(orderEvent, CancelReservationDto.class);
        if (cancelReservation != null) {
            reservationService.cancelReservation(cancelReservation);
        } else {
            LOGGER.error("Could not convert order event to cancel reservation DTO");
        }
    }
}
