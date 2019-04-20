package no.acntech.reservation.model;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Valid
public class UpdateReservationDto {

    @NotNull
    private UUID orderId;
    private UUID productId;
    private Long quantity;
    private ReservationStatus status;

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    @JsonIgnore
    @AssertTrue
    public boolean isValid() {
        return isValidUpdateQuantity() || isValidUpdateStatus();
    }

    private boolean isValidUpdateQuantity() {
        return productId != null && quantity != null;
    }

    private boolean isValidUpdateStatus() {
        return status != null && (status == ReservationStatus.CANCELED || status == ReservationStatus.REJECTED);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private UUID orderId;
        private UUID productId;
        private Long quantity;

        private ReservationStatus status;

        private Builder() {
        }

        public Builder orderId(UUID orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder productId(UUID productId) {
            this.productId = productId;
            return this;
        }

        public Builder quantity(Long quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder status(ReservationStatus status) {
            this.status = status;
            return this;
        }

        public UpdateReservationDto build() {
            UpdateReservationDto updateReservationDto = new UpdateReservationDto();
            updateReservationDto.orderId = this.orderId;
            updateReservationDto.quantity = this.quantity;
            updateReservationDto.productId = this.productId;
            updateReservationDto.status = this.status;
            return updateReservationDto;
        }
    }
}
