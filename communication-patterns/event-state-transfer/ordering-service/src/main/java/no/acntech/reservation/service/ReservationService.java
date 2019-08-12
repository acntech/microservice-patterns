package no.acntech.reservation.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import no.acntech.order.model.ItemStatus;
import no.acntech.order.service.OrderService;
import no.acntech.reservation.model.ReservationEvent;
import no.acntech.reservation.model.ReservationStatus;

@Service
public class ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);
    private final OrderService orderService;

    public ReservationService(final OrderService orderService) {
        this.orderService = orderService;
    }

    public void receiveReservationEvent(final ReservationEvent reservationEvent) {
        if (reservationEvent == null) {
            LOGGER.error("Received reservation event which was null");
        } else {
            try {
                processReservationEvent(reservationEvent);
            } catch (Exception e) {
                LOGGER.error("Error occurred while processing reservation event", e);
            }
        }
    }

    private void processReservationEvent(final ReservationEvent reservationEvent) {
        UUID reservationId = reservationEvent.getReservationId();
        Long quantity = reservationEvent.getQuantity();
        ReservationStatus reservationStatus = reservationEvent.getStatus();

        LOGGER.debug("Processing reservation event for reservation-id {}", reservationId);

        ItemStatus status = ItemStatus.valueOf(reservationStatus.name());

        orderService.updateItemReservation(reservationId, quantity, status);
    }
}
