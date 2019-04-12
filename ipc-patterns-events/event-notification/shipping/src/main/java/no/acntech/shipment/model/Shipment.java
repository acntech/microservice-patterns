package no.acntech.shipment.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Table(name = "SHIPMENTS")
@Entity
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(nullable = false)
    private UUID shipmentId;
    @NotNull
    @Column(nullable = false)
    private UUID orderId;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShipmentStatus status;
    @Column(nullable = false, updatable = false)
    private ZonedDateTime created;
    private ZonedDateTime modified;

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public UUID getShipmentId() {
        return shipmentId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public void setStatus(ShipmentStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public ZonedDateTime getModified() {
        return modified;
    }

    @PrePersist
    private void prePersist() {
        this.shipmentId = UUID.randomUUID();
        this.status = ShipmentStatus.CREATED;
        this.created = ZonedDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        this.modified = ZonedDateTime.now();
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
