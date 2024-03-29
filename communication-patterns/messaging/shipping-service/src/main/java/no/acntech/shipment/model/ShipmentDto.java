package no.acntech.shipment.model;

import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;

public class ShipmentDto {

    @NotNull
    private UUID shipmentId;
    @NotNull
    private UUID customerId;
    @NotNull
    private UUID orderId;
    @NotNull
    private ShipmentStatus status;
    @NotNull
    private ZonedDateTime created;
    private ZonedDateTime modified;

    public UUID getShipmentId() {
        return shipmentId;
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

    public ZonedDateTime getCreated() {
        return created;
    }

    public ZonedDateTime getModified() {
        return modified;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private UUID shipmentId;
        private UUID customerId;
        private UUID orderId;
        private ShipmentStatus status;
        private ZonedDateTime created;
        private ZonedDateTime modified;

        private Builder() {
        }

        public Builder shipmentId(UUID shipmentId) {
            this.shipmentId = shipmentId;
            return this;
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

        public Builder created(ZonedDateTime created) {
            this.created = created;
            return this;
        }

        public Builder modified(ZonedDateTime modified) {
            this.modified = modified;
            return this;
        }

        public ShipmentDto build() {
            var target = new ShipmentDto();
            target.shipmentId = this.shipmentId;
            target.customerId = this.customerId;
            target.orderId = this.orderId;
            target.status = this.status;
            target.created = this.created;
            target.modified = this.modified;
            return target;
        }
    }
}
