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
}
