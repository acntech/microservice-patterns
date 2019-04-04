package no.acntech.order.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(UUID orderId, UUID productId) {
        super("No item found with order-id " + orderId + " and product-id " + productId);
    }
}
