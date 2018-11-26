package no.acntech.product.exception;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(UUID productId) {
        super("No product found for product-id " + productId.toString());
    }
}
