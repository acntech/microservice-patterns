package no.acntech.shipment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ShipmentNotFoundException extends RuntimeException {

    public ShipmentNotFoundException(UUID shipmentId) {
        super("No shipment found for shipment-id " + shipmentId.toString());
    }
}
