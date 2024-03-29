package no.acntech.reservation.model;

import no.acntech.product.model.ProductEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.UUID;

@Table(name = "RESERVATIONS")
@Entity
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private UUID reservationId;
    @OneToOne
    @JoinColumn(name = "PRODUCT_ID")
    private ProductEntity product;
    @Column(nullable = false)
    private UUID orderId;
    @Column(nullable = false)
    private Long quantity;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;
    @Column(nullable = false, updatable = false)
    private ZonedDateTime created;
    @Column
    private ZonedDateTime modified;

    public Long getId() {
        return id;
    }

    public UUID getReservationId() {
        return reservationId;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public ZonedDateTime getModified() {
        return modified;
    }

    public boolean canUpdateStatus() {
        return status != null && status != ReservationStatus.CANCELED && status != ReservationStatus.CONFIRMED;
    }

    public void cancelReservation() {
        this.status = ReservationStatus.CANCELED;
    }

    @PrePersist
    private void prePersist() {
        reservationId = UUID.randomUUID();
        created = ZonedDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        modified = ZonedDateTime.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private ProductEntity product;
        private UUID orderId;
        private Long quantity;
        private ReservationStatus status;

        private Builder() {
        }

        public Builder product(ProductEntity product) {
            this.product = product;
            return this;
        }

        public Builder orderId(UUID orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder quantity(Long quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder statusReserved() {
            this.status = ReservationStatus.RESERVED;
            return this;
        }

        public Builder statusRejected() {
            this.status = ReservationStatus.REJECTED;
            return this;
        }

        public Builder statusFailed() {
            this.status = ReservationStatus.FAILED;
            return this;
        }

        public ReservationEntity build() {
            final var target = new ReservationEntity();
            target.product = this.product;
            target.orderId = this.orderId;
            target.quantity = this.quantity;
            target.status = this.status;
            return target;
        }
    }
}
