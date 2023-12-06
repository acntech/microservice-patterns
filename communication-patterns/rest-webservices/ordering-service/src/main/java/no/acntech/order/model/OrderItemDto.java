package no.acntech.order.model;

import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;
import java.util.UUID;

public class OrderItemDto {

    @NotNull
    private UUID itemId;
    @NotNull
    private UUID orderId;
    @NotNull
    private UUID productId;
    private UUID reservationId;
    @NotNull
    private Long quantity;
    @NotNull
    private OrderItemStatus status;
    @NotNull
    private ZonedDateTime created;
    private ZonedDateTime modified;

    public UUID getItemId() {
        return itemId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getProductId() {
        return productId;
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

    public ZonedDateTime getCreated() {
        return created;
    }

    public ZonedDateTime getModified() {
        return modified;
    }

    public boolean isReserved() {
        return OrderItemStatus.RESERVED.equals(status);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private UUID itemId;
        private UUID orderId;
        private UUID productId;
        private UUID reservationId;
        private Long quantity;
        private OrderItemStatus status;
        private ZonedDateTime created;
        private ZonedDateTime modified;

        private Builder() {
        }

        public Builder itemId(UUID itemId) {
            this.itemId = itemId;
            return this;
        }

        public Builder orderId(UUID orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder productId(UUID productId) {
            this.productId = productId;
            return this;
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

        public Builder created(ZonedDateTime created) {
            this.created = created;
            return this;
        }

        public Builder modified(ZonedDateTime modified) {
            this.modified = modified;
            return this;
        }

        public OrderItemDto build() {
            final var target = new OrderItemDto();
            target.itemId = this.itemId;
            target.orderId = this.orderId;
            target.modified = this.modified;
            target.productId = this.productId;
            target.reservationId = this.reservationId;
            target.status = this.status;
            target.quantity = this.quantity;
            target.created = this.created;
            return target;
        }
    }
}
