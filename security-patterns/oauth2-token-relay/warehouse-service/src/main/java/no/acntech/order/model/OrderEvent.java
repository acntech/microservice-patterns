package no.acntech.order.model;

import java.io.Serializable;
import java.util.UUID;

public class OrderEvent implements Serializable {

    private UUID orderId;

    public UUID getOrderId() {
        return orderId;
    }
}
