package no.acntech.reservation.model;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class PendingReservationDto {

    @NotNull
    private UUID reservationId;

    private PendingReservationDto() {
    }

    public UUID getReservationId() {
        return reservationId;
    }

    public static PendingReservationDto initWithReservationId() {
        final var target = new PendingReservationDto();
        target.reservationId = UUID.randomUUID();
        return target;
    }
}
