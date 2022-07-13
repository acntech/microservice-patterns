package no.acntech.reservation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.AssertTrue;

public class UpdateReservationDto {

    private Long quantity;
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
        if (status != null && status != ReservationStatus.CONFIRMED) {
            return false;
        }
        return quantity == null || quantity > 0;
    }
}
