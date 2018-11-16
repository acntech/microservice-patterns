package no.acntech.warehouse.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import no.acntech.warehouse.service.InventoryReservation;
import no.acntech.warehouse.service.InventoryService;

@RestController("reservations")
public class ReservationResource {

    private final InventoryService inventoryService;

    @Autowired
    public ReservationResource(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity reserve(@RequestBody InventoryReservation inventoryReservation) {
        inventoryService.reserve(inventoryReservation);
        return ResponseEntity.ok().build();
    }
}
