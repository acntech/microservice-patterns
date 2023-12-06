package no.acntech.reservation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

public class UpdateReservationDto {

    @NotNull
    private Long quantity;
    @NotNull
    private ReservationStatus status;

    public Long getQuantity() {
        return quantity;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    @JsonIgnore
    @AssertTrue
    public boolean isValid() {
        return quantity > 0 || ReservationStatus.CONFIRMED.equals(status);
    }
}
