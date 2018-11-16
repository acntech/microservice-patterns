package no.acntech.warehouse.service.exception;

public class OutOfStockException extends RuntimeException {

    public OutOfStockException(String productId, int quantity) {
        super("Don't have quantity of " + quantity + " for productId " + productId);
    }
}
