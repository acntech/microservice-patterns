package no.acntech.reservation.model;

import java.util.UUID;

public class ReservationEvent {

    private UUID reservationId;

    public UUID getReservationId() {
        return reservationId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private UUID reservationId;

        private Builder() {
        }

        public Builder reservationId(UUID reservationId) {
            this.reservationId = reservationId;
            return this;
        }

        public ReservationEvent build() {
            ReservationEvent reservationEvent = new ReservationEvent();
            reservationEvent.reservationId = this.reservationId;
            return reservationEvent;
        }
    }
}
