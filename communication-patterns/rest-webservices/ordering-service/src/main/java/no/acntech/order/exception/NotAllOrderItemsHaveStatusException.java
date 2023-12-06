package no.acntech.order.exception;

import no.acntech.order.model.OrderItemStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class NotAllOrderItemsHaveStatusException extends IllegalStateException {

    public NotAllOrderItemsHaveStatusException(UUID orderId, OrderItemStatus status) {
        super("Not all order items have status " + status.name() + " for order-id " + orderId);
    }
}
