package no.acntech.order.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ItemAlreadyExistsException extends RuntimeException {

    public ItemAlreadyExistsException(UUID orderId, UUID productId) {
        super("Item already exists for order-id " + orderId + " and product-id " + productId);
    }
}
