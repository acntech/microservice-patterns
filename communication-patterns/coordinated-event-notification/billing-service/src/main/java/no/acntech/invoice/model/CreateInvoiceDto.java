package no.acntech.invoice.model;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class CreateInvoiceDto {

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

        public CreateInvoiceDto build() {
            final var target = new CreateInvoiceDto();
            target.customerId = this.customerId;
            target.orderId = this.orderId;
            return target;
        }
    }
}
