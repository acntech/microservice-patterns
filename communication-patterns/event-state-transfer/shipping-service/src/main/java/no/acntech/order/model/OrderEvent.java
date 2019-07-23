package no.acntech.order.model;

import java.io.Serializable;
import java.util.UUID;

public class OrderEvent implements Serializable {

    private OrderEventType type;
    private UUID orderId;
    private UUID productId;
    private Long quantity;

    public OrderEventType getType() {
        return type;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
