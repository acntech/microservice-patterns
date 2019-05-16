package no.acntech.reservation.model;

import java.util.UUID;

public class SaveReservation {

    private UUID orderId;
    private UUID productId;
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

        public SaveReservation build() {
            SaveReservation saveReservation = new SaveReservation();
            saveReservation.orderId = this.orderId;
            saveReservation.productId = this.productId;
            saveReservation.quantity = this.quantity;
            return saveReservation;
        }
    }
}
