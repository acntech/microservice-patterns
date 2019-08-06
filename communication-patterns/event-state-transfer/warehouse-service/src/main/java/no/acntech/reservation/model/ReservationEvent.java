package no.acntech.reservation.model;

import java.util.UUID;

public class ReservationEvent {

    private ReservationEventType type;
    private UUID orderId;
    private UUID productId;

    public ReservationEventType getType() {
        return type;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getProductId() {
        return productId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private ReservationEventType type;
        private UUID orderId;

        private UUID productId;

        private Builder() {
        }

        public Builder type(ReservationEventType type) {
            this.type = type;
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

        public ReservationEvent build() {
            ReservationEvent reservationEvent = new ReservationEvent();
            reservationEvent.productId = this.productId;
            reservationEvent.orderId = this.orderId;
            reservationEvent.type = this.type;
            return reservationEvent;
        }
    }
}
