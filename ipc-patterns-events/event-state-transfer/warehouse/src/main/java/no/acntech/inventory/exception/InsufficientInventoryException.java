package no.acntech.inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class InsufficientInventoryException extends RuntimeException {

    public InsufficientInventoryException(UUID productId) {
        super("Reservation for product " + productId + " is not sufficient to fulfill reservation");
    }
}
