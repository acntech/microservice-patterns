package no.acntech.reservation.model;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.UUID;

@Valid
public class UpdateReservationDto {

    @NotNull
    private UUID orderId;
    @NotNull
    private UUID productId;
    @Min(1)
    @NotNull
    private Long quantity;

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private UUID orderId;
        private UUID productId;
        private Long quantity;

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

        public UpdateReservationDto build() {
            UpdateReservationDto updateReservationDto = new UpdateReservationDto();
            updateReservationDto.orderId = this.orderId;
            updateReservationDto.productId = this.productId;
            updateReservationDto.quantity = this.quantity;
            return updateReservationDto;
        }
    }
}
