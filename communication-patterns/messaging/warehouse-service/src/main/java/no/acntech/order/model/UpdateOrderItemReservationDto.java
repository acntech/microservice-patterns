package no.acntech.order.model;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class UpdateOrderItemReservationDto {

    @NotNull
    private UUID orderId;
    @NotNull
    private UUID productId;
    @NotNull
    private OrderItemStatus status;

    private UpdateOrderItemReservationDto() {
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getProductId() {
        return productId;
    }

    public OrderItemStatus getStatus() {
        return status;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private UUID orderId;
        private UUID productId;
        private OrderItemStatus status;

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

        public Builder status(OrderItemStatus status) {
            this.status = status;
            return this;
        }

        public UpdateOrderItemReservationDto build() {
            final var target = new UpdateOrderItemReservationDto();
            target.orderId = this.orderId;
            target.productId = this.productId;
            target.status = this.status;
            return target;
        }
    }
}
