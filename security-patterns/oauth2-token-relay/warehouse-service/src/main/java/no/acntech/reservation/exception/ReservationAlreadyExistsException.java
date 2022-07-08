package no.acntech.reservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ReservationAlreadyExistsException extends RuntimeException {

    public ReservationAlreadyExistsException(UUID orderId, UUID productId) {
        super("Reservation already exists for order-id " + orderId + " and product-id " + productId);
    }
}
