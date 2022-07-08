package no.acntech.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ProductNotAvailableException extends RuntimeException {

    public ProductNotAvailableException(UUID orderId, UUID productId) {
        super("Product not available for order-id " + orderId + " and product-id " + productId);
    }
}
