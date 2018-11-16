package no.acntech.order.messaging.listeners;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import no.acntech.messaging.types.warehouse.InventoryReservationConfirmationMessage;
import no.acntech.order.service.OrderService;

@Component
@Transactional
public class WarehouseMessageListener {

    private final OrderService orderService;

    @Autowired
    public WarehouseMessageListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @JmsListener(destination = "${queue.warehouse.reservation.confirmation}")
    public void receiveReservationConfirmation(InventoryReservationConfirmationMessage inventoryReservationConfirmationMessage) {
        orderService.warehouseReservationConfirmed(inventoryReservationConfirmationMessage);
    }

}
