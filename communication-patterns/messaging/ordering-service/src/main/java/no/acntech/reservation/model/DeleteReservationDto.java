package no.acntech.reservation.model;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class DeleteReservationDto {

    @NotNull
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

        public DeleteReservationDto build() {
            final var target = new DeleteReservationDto();
            target.reservationId = this.reservationId;
            return target;
        }
    }
}
