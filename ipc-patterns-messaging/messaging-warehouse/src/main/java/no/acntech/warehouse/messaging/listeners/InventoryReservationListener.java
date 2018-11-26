package no.acntech.warehouse.messaging.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import no.acntech.messaging.types.warehouse.InventoryReservationMessage;
import no.acntech.warehouse.service.WarehouseService;

@Component
public class InventoryReservationListener {

    private final WarehouseService warehouseService;

    @Autowired
    public InventoryReservationListener(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @JmsListener(destination = "${queue.warehouse.reservation.reserve}")
    public void reserve(InventoryReservationMessage inventoryReservationMessage) {
        warehouseService.reserveAndConfirm(inventoryReservationMessage);
    }

}
