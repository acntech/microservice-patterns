package no.acntech.reservation.model;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

public class ReservationEvent implements Serializable {

    @NotNull
    private UUID reservationId;

    public UUID getReservationId() {
        return reservationId;
    }
}
