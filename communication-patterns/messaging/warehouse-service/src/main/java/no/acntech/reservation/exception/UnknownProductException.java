package no.acntech.reservation.exception;

public class UnknownProductException extends RuntimeException {

    public UnknownProductException(String productId) {
        super("Unknown product: " + productId);
    }
}
