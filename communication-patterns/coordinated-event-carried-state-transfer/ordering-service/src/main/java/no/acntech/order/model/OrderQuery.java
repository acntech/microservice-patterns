package no.acntech.order.model;

import java.util.UUID;

public class OrderQuery {

    private UUID customerId;
    private OrderStatus status;

    public UUID getCustomerId() {
        return customerId;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
