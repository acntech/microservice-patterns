package no.acntech.order.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.PRECONDITION_FAILED)
public class ReservationIdNotReceivedException extends IllegalStateException {

    public ReservationIdNotReceivedException(UUID orderId, UUID productId) {
        super("Could not retrieve reservation-id for order-id " + orderId + " and product-id " + productId);
    }
}
