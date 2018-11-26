package no.acntech.invoice.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;

@Table(name = "INVOICES")
@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private UUID invoiceId;
    @NotNull
    private UUID orderId;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;
    @CreatedDate
    private ZonedDateTime created;
    @LastModifiedDate
    private ZonedDateTime modified;

    public Long getId() {
        return id;
    }

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public ZonedDateTime getModified() {
        return modified;
    }

    @PrePersist
    public void prePersist() {
        this.invoiceId = UUID.randomUUID();
        this.status = Status.CREATED;
        this.created = ZonedDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.modified = ZonedDateTime.now();
    }

    public enum Status {
        CREATED,
        PENDING,
        COMPLETED
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

        public Invoice build() {
            Invoice invoice = new Invoice();
            invoice.orderId = this.orderId;
            return invoice;
        }
    }
}
