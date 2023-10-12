package no.acntech.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class OrderItemNotFoundException extends RuntimeException {

    public OrderItemNotFoundException(UUID itemId) {
        super("No order item found for item-id " + itemId);
    }
}
