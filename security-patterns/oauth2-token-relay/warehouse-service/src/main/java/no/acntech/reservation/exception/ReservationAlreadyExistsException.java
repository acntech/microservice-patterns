package no.acntech.reservation.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ReservationAlreadyExistsException extends RuntimeException {

    public ReservationAlreadyExistsException(UUID orderId, UUID productId) {
        super("Reservation already exists for order-id " + orderId + " and product-id " + productId);
    }
}
