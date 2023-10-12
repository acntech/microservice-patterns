package no.acntech.reservation.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Valid
public class ReservationEvent {

    @NotNull
    private UUID eventId;
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

    public UUID getEventId() {
        return eventId;
    }

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
            final var target = new ReservationEvent();
            target.eventId = UUID.randomUUID();
            target.eventType = this.eventType;
            target.reservationId = this.reservationId;
            target.status = this.status;
            target.orderId = this.orderId;
            target.productId = this.productId;
            target.quantity = this.quantity;
            return target;
        }
    }
}
