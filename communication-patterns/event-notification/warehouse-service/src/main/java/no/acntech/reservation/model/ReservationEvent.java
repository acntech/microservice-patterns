package no.acntech.reservation.model;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class ReservationEvent {

    @NotNull
    private UUID eventId;
    @NotNull
    private UUID reservationId;

    public UUID getEventId() {
        return eventId;
    }

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
            final var target = new ReservationEvent();
            target.eventId = UUID.randomUUID();
            target.reservationId = this.reservationId;
            return target;
        }
    }
}
