package no.acntech.order.model;

import java.util.UUID;

public class CreateOrderLine {

    private UUID productId;
    private Long quantity;

    public UUID getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
