package no.acntech.reservation.listener;

import javax.transaction.Transactional;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import no.acntech.order.service.OrderService;
import no.acntech.reservation.model.InventoryReservationConfirmationMessage;

@Transactional
@Component
public class ReservationMessageListener {

    private final OrderService orderService;

    public ReservationMessageListener(final OrderService orderService) {
        this.orderService = orderService;
    }

    @JmsListener(destination = "${queue.warehouse.reservation.confirmation}")
    public void receiveReservationConfirmation(InventoryReservationConfirmationMessage message) {
        orderService.warehouseReservationConfirmed(message);
    }

}
