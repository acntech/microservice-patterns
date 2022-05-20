package no.acntech.shipment.model;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

@Table(name = "SHIPMENTS")
@Entity
public class ShipmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private UUID shipmentId;
    @Column(nullable = false)
    private UUID customerId;
    @Column(nullable = false)
    private UUID orderId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShipmentStatus status;
    @Column(nullable = false, updatable = false)
    private ZonedDateTime created;
    @Column
    private ZonedDateTime modified;

    public Long getId() {
        return id;
    }

    public UUID getShipmentId() {
        return shipmentId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public ShipmentStatus getStatus() {
        return status;
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

        public ShipmentEntity build() {
            ShipmentEntity shipment = new ShipmentEntity();
            shipment.customerId = this.customerId;
            shipment.orderId = this.orderId;
            return shipment;
        }
    }
}
