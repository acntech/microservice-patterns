package no.acntech.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class InventoryQuantityNotSufficientException extends RuntimeException {

    public InventoryQuantityNotSufficientException(UUID productId) {
        super("Inventory for product " + productId + " is not sufficient to fulfill reservation");
    }
}
