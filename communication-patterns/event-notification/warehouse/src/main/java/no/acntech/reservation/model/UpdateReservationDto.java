package no.acntech.reservation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;

@Valid
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
        return (quantity != null && quantity > 0) ||
                (ReservationStatus.CONFIRMED.equals(status));
    }
}
