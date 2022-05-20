package no.acntech.reservation.model;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class PendingReservationDto {

    @NotNull
    private UUID reservationId;

    public UUID getReservationId() {
        return reservationId;
    }
}
