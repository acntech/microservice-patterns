package no.acntech.reservation.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.UUID;

@Valid
public class CancelReservationDto {

    @NotNull
    private UUID orderId;
    private UUID productId;

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getProductId() {
        return productId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private UUID orderId;
        private UUID productId;

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

        public CancelReservationDto build() {
            CancelReservationDto updateReservationDto = new CancelReservationDto();
            updateReservationDto.orderId = this.orderId;
            updateReservationDto.productId = this.productId;
            return updateReservationDto;
        }
    }
}
