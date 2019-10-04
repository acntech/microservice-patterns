package no.acntech.reservation.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.UUID;

@Valid
public class ReservationEvent {

    @NotNull
    private ReservationEventType eventType;
    @NotNull
    private UUID reservationId;
    @NotNull
    private ReservationStatus status;
    @NotNull
    private UUID orderId;
    @NotNull
    private UUID productId;
    @NotNull
    private Long quantity;

    public ReservationEventType getEventType() {
        return eventType;
    }

    public UUID getReservationId() {
        return reservationId;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private ReservationEventType eventType;
        private UUID reservationId;
        private ReservationStatus status;
        private UUID orderId;
        private UUID productId;
        private Long quantity;

        private Builder() {
        }

        public Builder eventType(ReservationEventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder reservationId(UUID reservationId) {
            this.reservationId = reservationId;
            return this;
        }

        public Builder status(ReservationStatus status) {
            this.status = status;
            return this;
        }

        public Builder orderId(UUID orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder productId(UUID productId) {
            this.productId = productId;
            return this;
        }

        public Builder quantity(Long quantity) {
            this.quantity = quantity;
            return this;
        }

        public ReservationEvent build() {
            ReservationEvent reservationEvent = new ReservationEvent();
            reservationEvent.eventType = this.eventType;
            reservationEvent.reservationId = this.reservationId;
            reservationEvent.status = this.status;
            reservationEvent.orderId = this.orderId;
            reservationEvent.productId = this.productId;
            reservationEvent.quantity = this.quantity;
            return reservationEvent;
        }
    }
}
