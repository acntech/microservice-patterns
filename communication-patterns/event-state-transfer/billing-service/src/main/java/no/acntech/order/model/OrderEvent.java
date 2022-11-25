package no.acntech.order.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Valid
public class OrderEvent implements Serializable {

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
}
