package no.acntech.shipment.model;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class CreateShipmentDto {

    @NotNull
    private UUID customerId;
    @NotNull
    private UUID orderId;

    public UUID getCustomerId() {
        return customerId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private UUID customerId;
        private UUID orderId;

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

        public CreateShipmentDto build() {
            var target = new CreateShipmentDto();
            target.customerId = this.customerId;
            target.orderId = this.orderId;
            return target;
        }
    }
}
