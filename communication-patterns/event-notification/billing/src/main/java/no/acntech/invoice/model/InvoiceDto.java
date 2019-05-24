package no.acntech.invoice.model;

import javax.validation.constraints.NotNull;

import java.time.ZonedDateTime;
import java.util.UUID;

public class InvoiceDto {

    @NotNull
    private UUID invoiceId;
    @NotNull
    private UUID orderId;
    @NotNull
    private InvoiceStatus status;
    @NotNull
    private ZonedDateTime created;
    private ZonedDateTime modified;

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public InvoiceStatus getStatus() {
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

        private UUID invoiceId;
        private UUID orderId;
        private InvoiceStatus status;
        private ZonedDateTime created;
        private ZonedDateTime modified;

        private Builder() {
        }

        public Builder invoiceId(UUID invoiceId) {
            this.invoiceId = invoiceId;
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

        public Builder created(ZonedDateTime created) {
            this.created = created;
            return this;
        }

        public Builder modified(ZonedDateTime modified) {
            this.modified = modified;
            return this;
        }

        public InvoiceDto build() {
            InvoiceDto invoiceDto = new InvoiceDto();
            invoiceDto.invoiceId = this.invoiceId;
            invoiceDto.orderId = this.orderId;
            invoiceDto.modified = this.modified;
            invoiceDto.status = this.status;
            invoiceDto.created = this.created;
            return invoiceDto;
        }
    }
}
