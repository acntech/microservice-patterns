package no.acntech.warehouse.resource;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import no.acntech.warehouse.service.InventoryReservation;
import no.acntech.warehouse.service.InventoryService;
import no.acntech.warehouse.service.exception.OutOfStockException;
import no.acntech.warehouse.service.exception.UnknownProductException;

@RestController("reservations")
public class ReservationResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationResource.class);

    private final InventoryService inventoryService;

    @Autowired
    public ReservationResource(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity reserve(@RequestBody InventoryReservation inventoryReservation) {
        try {
            inventoryService.reserve(inventoryReservation);
        } catch (OutOfStockException | UnknownProductException e){
            LOGGER.error("Error in order reservation - returning 500", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(UUID.randomUUID().toString());
    }
}
