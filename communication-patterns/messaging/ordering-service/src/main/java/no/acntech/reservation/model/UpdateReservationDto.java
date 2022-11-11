package no.acntech.reservation.model;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class UpdateReservationDto {

    @NotNull
    private UUID reservationId;
    private Long quantity;
    private ReservationStatus status;

    public UUID getReservationId() {
        return reservationId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private UUID reservationId;
        private Long quantity;
        private ReservationStatus status;

        private Builder() {
        }

        public Builder reservationId(UUID reservationId) {
            this.reservationId = reservationId;
            return this;
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
            final var target = new UpdateReservationDto();
            target.reservationId = this.reservationId;
            target.quantity = this.quantity;
            target.status = this.status;
            return target;
        }
    }
}
