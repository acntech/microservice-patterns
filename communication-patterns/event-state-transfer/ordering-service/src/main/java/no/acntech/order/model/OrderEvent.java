package no.acntech.order.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Valid
public class OrderEvent implements Serializable {

    @NotNull
    private UUID eventId;
    @NotNull
    private OrderEventType eventType;
    @NotNull
    private UUID customerId;
    @NotNull
    private UUID orderId;
    @NotNull
    private OrderStatus orderStatus;
    private UUID productId;
    private Long quantity;
    private OrderItemStatus itemStatus;

    public UUID getEventId() {
        return eventId;
    }

    public OrderEventType getEventType() {
        return eventType;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public UUID getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public OrderItemStatus getItemStatus() {
        return itemStatus;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private OrderEventType eventType;
        private UUID customerId;
        private UUID orderId;
        private OrderStatus orderStatus;
        private UUID productId;
        private Long quantity;

        private OrderItemStatus itemStatus;

        private Builder() {
        }

        public Builder eventType(OrderEventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder customerId(UUID customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder orderId(UUID orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder orderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
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

        public Builder itemStatus(OrderItemStatus itemStatus) {
            this.itemStatus = itemStatus;
            return this;
        }

        public OrderEvent build() {
            final var target = new OrderEvent();
            target.eventId = UUID.randomUUID();
            target.eventType = this.eventType;
            target.customerId = this.customerId;
            target.orderId = this.orderId;
            target.orderStatus = this.orderStatus;
            target.productId = this.productId;
            target.quantity = this.quantity;
            target.itemStatus = this.itemStatus;
            return target;
        }
    }
}
