package no.acntech.shipment.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;

@Table(name = "SHIPMENTS")
@Entity
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private UUID shipmentId;
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

    public UUID getShipmentId() {
        return shipmentId;
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
        this.shipmentId = UUID.randomUUID();
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

        public Shipment build() {
            Shipment shipment = new Shipment();
            shipment.orderId = this.orderId;
            return shipment;
        }
    }
}
