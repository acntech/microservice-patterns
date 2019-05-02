package no.acntech.reservation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import no.acntech.product.model.Product;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;

@Table(name = "RESERVATIONS")
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(nullable = false)
    private UUID reservationId;
    @OneToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;
    @NotNull
    @Column(nullable = false)
    private UUID orderId;
    @NotNull
    @Column(nullable = false)
    private Long quantity;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;
    @NotNull
    @Column(nullable = false, updatable = false)
    private ZonedDateTime created;
    private ZonedDateTime modified;

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public UUID getReservationId() {
        return reservationId;
    }

    public Product getProduct() {
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

    @PrePersist
    private void prePersist() {
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

        private UUID reservationId;
        private Product product;
        private UUID orderId;
        private Long quantity;
        private ReservationStatus status;

        private Builder() {
        }

        public Builder reservationId(UUID reservationId) {
            this.reservationId = reservationId;
            return this;
        }

        public Builder product(Product product) {
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

        public Reservation build() {
            Reservation reservation = new Reservation();
            reservation.reservationId = this.reservationId;
            reservation.product = this.product;
            reservation.orderId = this.orderId;
            reservation.quantity = this.quantity;
            reservation.status = this.status;
            return reservation;
        }
    }
}
