package no.acntech.inventory.exception;

import java.util.UUID;

public class InventoryQuantityNotSufficientException extends RuntimeException {

    public InventoryQuantityNotSufficientException(UUID productId) {
        super("Inventory for product " + productId + " is not sufficient to fulfill reservation");
    }
}
