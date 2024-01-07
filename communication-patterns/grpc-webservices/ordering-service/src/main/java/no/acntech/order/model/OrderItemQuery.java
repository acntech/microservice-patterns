package no.acntech.order.model;

import java.util.UUID;

public class OrderItemQuery {

    private UUID orderId;

    private OrderItemQuery() {
    }

    public UUID getOrderId() {
        return orderId;
    }

    public static Builder build() {
        return new Builder();
    }

    public static final class Builder {

        private UUID orderId;

        private Builder() {
        }

        public Builder orderId(UUID customerId) {
            this.orderId = customerId;
            return this;
        }

        public OrderItemQuery build() {
            final var target = new OrderItemQuery();
            target.orderId = this.orderId;
            return target;
        }
    }
}
