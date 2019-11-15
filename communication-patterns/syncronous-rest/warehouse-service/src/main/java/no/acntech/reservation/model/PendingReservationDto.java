package no.acntech.reservation.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Valid
public class PendingReservationDto {

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
            reservationId = UUID.randomUUID();
        }

        public PendingReservationDto build() {
            PendingReservationDto reservationDto = new PendingReservationDto();
            reservationDto.reservationId = this.reservationId;
            return reservationDto;
        }
    }
}
