package no.acntech.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ItemAlreadyExistsException extends IllegalStateException {

    public ItemAlreadyExistsException(UUID orderId, UUID productId) {
        super("Item already exists for order-id " + orderId + " and product-id " + productId);
    }
}
