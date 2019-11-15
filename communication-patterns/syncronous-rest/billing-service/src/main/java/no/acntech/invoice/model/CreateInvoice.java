package no.acntech.invoice.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.UUID;

@Valid
public class CreateInvoice {

    @NotNull
    private UUID orderId;

    @NotNull
    private UUID customerId;

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private UUID orderId;
        private UUID customerId;

        private Builder() {
        }

        public Builder orderId(UUID orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder customerId(UUID customerId) {
            this.customerId = customerId;
            return this;
        }

        public CreateInvoice build() {
            CreateInvoice createInvoice = new CreateInvoice();
            createInvoice.orderId = this.orderId;
            createInvoice.customerId = this.customerId;
            return createInvoice;
        }
    }
}
