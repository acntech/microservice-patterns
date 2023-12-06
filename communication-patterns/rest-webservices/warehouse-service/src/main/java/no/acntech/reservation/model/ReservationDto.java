package no.acntech.reservation.model;

import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;

public class ReservationDto {

    @NotNull
    private UUID reservationId;
    @NotNull
    private UUID productId;
    @NotNull
    private UUID orderId;
    @NotNull
    private Long quantity;
    @NotNull
    private ReservationStatus status;
    @NotNull
    private ZonedDateTime created;
    private ZonedDateTime modified;

    public UUID getReservationId() {
        return reservationId;
    }

    public UUID getProductId() {
        return productId;
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

        private UUID reservationId;
        private UUID productId;
        private UUID orderId;
        private Long quantity;
        private ReservationStatus status;
        private ZonedDateTime created;
        private ZonedDateTime modified;

        private Builder() {
        }

        public Builder reservationId(UUID reservationId) {
            this.reservationId = reservationId;
            return this;
        }

        public Builder productId(UUID productId) {
            this.productId = productId;
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

        public Builder status(ReservationStatus status) {
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

        public ReservationDto build() {
            final var target = new ReservationDto();
            target.reservationId = this.reservationId;
            target.productId = this.productId;
            target.orderId = this.orderId;
            target.quantity = quantity;
            target.status = this.status;
            target.modified = this.modified;
            target.created = this.created;
            return target;
        }
    }
}
