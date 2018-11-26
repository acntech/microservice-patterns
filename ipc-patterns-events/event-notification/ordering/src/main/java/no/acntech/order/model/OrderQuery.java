package no.acntech.order.model;

import java.util.UUID;

public class OrderQuery {

    private UUID customerId;
    private Order.Status status;

    public UUID getCustomerId() {
        return customerId;
    }

    public Order.Status getStatus() {
        return status;
    }
}
