package no.acntech.reservation.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Valid
public class CancelReservationDto {

    @NotNull
    private UUID orderId;
    private UUID productId;

    private CancelReservationDto() {
    }

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
            final var target = new CancelReservationDto();
            target.orderId = this.orderId;
            target.productId = this.productId;
            return target;
        }
    }
}
