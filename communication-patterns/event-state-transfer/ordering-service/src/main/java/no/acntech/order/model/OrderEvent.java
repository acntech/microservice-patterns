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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private OrderEventType type;
        private UUID orderId;
        private UUID productId;
        private Long quantity;

        private Builder() {
        }

        public Builder type(OrderEventType type) {
            this.type = type;
            return this;
        }

        public Builder orderId(UUID orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder productId(UUID productId) {
            this.productId = productId;
            return this;
        }

        public Builder quantity(Long quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderEvent build() {
            OrderEvent orderEvent = new OrderEvent();
            orderEvent.type = this.type;
            orderEvent.orderId = this.orderId;
            orderEvent.productId = this.productId;
            orderEvent.quantity = this.quantity;
            return orderEvent;
        }
    }
}
