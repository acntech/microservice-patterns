package no.acntech.shipment.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.UUID;

@Valid
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
            CreateShipmentDto createShipment = new CreateShipmentDto();
            createShipment.customerId = this.customerId;
            createShipment.orderId = this.orderId;
            return createShipment;
        }
    }
}
