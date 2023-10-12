package no.acntech.order.model;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class UpdateOrderItemDto {

    private UUID reservationId;
    @NotNull
    private Long quantity;
    private OrderItemStatus status;

    private UpdateOrderItemDto() {
    }

    public UUID getReservationId() {
        return reservationId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public OrderItemStatus getStatus() {
        return status;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private UUID reservationId;
        private Long quantity;
        private OrderItemStatus status;

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

        public Builder status(OrderItemStatus status) {
            this.status = status;
            return this;
        }

        public UpdateOrderItemDto build() {
            final var target = new UpdateOrderItemDto();
            target.quantity = this.quantity;
            target.status = this.status;
            target.reservationId = this.reservationId;
            return target;
        }
    }
}
