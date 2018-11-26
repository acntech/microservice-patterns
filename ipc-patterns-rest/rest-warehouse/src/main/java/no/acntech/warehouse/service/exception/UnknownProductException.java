package no.acntech.warehouse.service.exception;

public class UnknownProductException extends RuntimeException {

    public UnknownProductException(String productId) {
        super("Unknown product: " + productId);
    }
}
