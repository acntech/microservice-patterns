package no.acntech.reservation.listener;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import no.acntech.reservation.model.InventoryReservationMessage;
import no.acntech.reservation.service.ReservationService;

@Component
public class InventoryReservationListener {

    private final ReservationService reservationService;

    public InventoryReservationListener(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @JmsListener(destination = "${queue.warehouse.reservation.reserve}")
    public void reserve(InventoryReservationMessage message) {
        reservationService.reserveAndConfirm(message);
    }
}
