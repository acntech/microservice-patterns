package no.acntech.shipment.model;

import java.util.UUID;

public class ShipmentQuery {

    private UUID customerId;
    private UUID orderId;
    private ShipmentStatus status;

    public UUID getCustomerId() {
        return customerId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public ShipmentStatus getStatus() {
        return status;
    }
}
