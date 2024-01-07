package no.acntech.invoice.model;

import java.util.UUID;

public class InvoiceQuery {

    private UUID customerId;
    private UUID orderId;
    private InvoiceStatus status;

    private InvoiceQuery() {
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private UUID customerId;
        private UUID orderId;

        private InvoiceStatus status;

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

        public Builder status(InvoiceStatus status) {
            this.status = status;
            return this;
        }

        public InvoiceQuery build() {
            final var target = new InvoiceQuery();
            target.customerId = this.customerId;
            target.orderId = this.orderId;
            target.status = this.status;
            return target;
        }
    }
}
