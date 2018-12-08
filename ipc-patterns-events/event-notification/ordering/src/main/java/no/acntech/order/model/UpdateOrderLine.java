package no.acntech.order.model;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Valid
public class UpdateOrderLine {

    @NotNull
    private UUID orderId;
    @NotNull
    private UUID productId;
    @Min(1)
    private Long quantity;
    @NotNull
    private OrderLineStatus status;

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public OrderLineStatus getStatus() {
        return status;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private UUID orderId;
        private UUID productId;
        private Long quantity;
        private OrderLineStatus status;

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

        public Builder status(OrderLineStatus status) {
            this.status = status;
            return this;
        }

        public UpdateOrderLine build() {
            UpdateOrderLine updateOrderLine = new UpdateOrderLine();
            updateOrderLine.productId = this.productId;
            updateOrderLine.orderId = this.orderId;
            updateOrderLine.status = this.status;
            updateOrderLine.quantity = this.quantity;
            return updateOrderLine;
        }
    }
}
