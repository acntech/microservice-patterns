package no.acntech.order.model;

import java.util.UUID;

public class OrderQuery {

    private UUID customerId;
    private OrderStatus status;

    private OrderQuery() {
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public static Builder build() {
        return new Builder();
    }

    public static final class Builder {

        private UUID customerId;
        private OrderStatus status;

        private Builder() {
        }

        public Builder customerId(UUID customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public OrderQuery build() {
            final var target = new OrderQuery();
            target.customerId = this.customerId;
            target.status = this.status;
            return target;
        }
    }
}
