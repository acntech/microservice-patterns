package no.acntech.shipping.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("shipments")
public class ShippingResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShippingResource.class);

    @PostMapping
    public ResponseEntity ship(@RequestBody String warehouseReservationId) {
        if (warehouseReservationId.contains("13")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        LOGGER.info("Reservation with reservationId={} shipped!", warehouseReservationId);
        return ResponseEntity.ok().build();
    }
}
