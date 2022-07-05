package no.acntech.order.model;

import javax.validation.constraints.NotNull;

public class UpdateOrderItemDto {

    @NotNull
    private Long quantity;
    private OrderItemStatus status;

    private UpdateOrderItemDto() {
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

        private Long quantity;
        private OrderItemStatus status;

        private Builder() {
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
            return target;
        }
    }
}
