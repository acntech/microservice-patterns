package no.acntech.product.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ProductNotAvailableException extends RuntimeException {

    public ProductNotAvailableException(UUID orderId, UUID productId) {
        super("Product not available for order-id " + orderId + " and product-id " + productId);
    }
}
