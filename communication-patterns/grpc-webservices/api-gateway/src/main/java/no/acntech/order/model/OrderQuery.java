package no.acntech.order.model;

import java.util.UUID;

public class OrderQuery {

    private UUID customerId;
    private OrderStatus status;

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
