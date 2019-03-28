package no.acntech.order.model;

import java.io.Serializable;
import java.util.UUID;

public class OrderEvent implements Serializable {

    private OrderEventType type;
    private UUID orderId;

    public OrderEventType getType() {
        return type;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private OrderEventType type;
        private UUID orderId;

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

        public OrderEvent build() {
            OrderEvent orderEvent = new OrderEvent();
            orderEvent.type = this.type;
            orderEvent.orderId = this.orderId;
            return orderEvent;
        }
    }
}
