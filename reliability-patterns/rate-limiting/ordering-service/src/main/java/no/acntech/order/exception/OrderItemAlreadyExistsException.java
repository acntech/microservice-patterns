package no.acntech.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class OrderItemAlreadyExistsException extends IllegalStateException {

    public OrderItemAlreadyExistsException(UUID orderId, UUID productId) {
        super("Order item already exists for order-id " + orderId + " and product-id " + productId);
    }
}
