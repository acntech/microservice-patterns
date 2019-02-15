package no.acntech.shipment.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Valid
public class CreateShipment {

    @NotNull
    private UUID orderId;

    public UUID getOrderId() {
        return orderId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {


        private UUID orderId;

        private Builder() {
        }

        public Builder orderId(UUID orderId) {
            this.orderId = orderId;
            return this;
        }

        public CreateShipment build() {
            CreateShipment createShipment = new CreateShipment();
            createShipment.orderId = this.orderId;
            return createShipment;
        }
    }
}
