package no.acntech.shipment.model;

import java.util.UUID;

public class ShipmentQuery {

    private UUID customerId;
    private UUID orderId;
    private ShipmentStatus status;

    private ShipmentQuery() {
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private UUID customerId;
        private UUID orderId;

        private ShipmentStatus status;

        private Builder() {
        }

        public Builder customerId(UUID customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder orderId(UUID orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder status(ShipmentStatus status) {
            this.status = status;
            return this;
        }

        public ShipmentQuery build() {
            final var target = new ShipmentQuery();
            target.customerId = this.customerId;
            target.orderId = this.orderId;
            target.status = this.status;
            return target;
        }
    }
}
