package no.acntech.reservation.model;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Long quantity;

        private ReservationStatus status;

        private Builder() {
        }

        public Builder quantity(Long quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder statusConfirmed() {
            this.status = ReservationStatus.CONFIRMED;
            return this;
        }

        public UpdateReservationDto build() {
            UpdateReservationDto updateReservationDto = new UpdateReservationDto();
            updateReservationDto.quantity = this.quantity;
            updateReservationDto.status = this.status;
            return updateReservationDto;
        }
    }
}
