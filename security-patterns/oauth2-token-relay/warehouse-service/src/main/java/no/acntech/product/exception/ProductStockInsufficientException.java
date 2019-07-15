package no.acntech.product.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ProductStockInsufficientException extends RuntimeException {

    public ProductStockInsufficientException(UUID orderId, UUID productId) {
        super("Product stock insufficient for order-id " + orderId + " and product-id " + productId);
    }
}
