package no.acntech.shipment.model;

import java.util.UUID;

public class ShipmentQuery {

    private UUID orderId;
    private Shipment.Status status;

    public UUID getOrderId() {
        return orderId;
    }

    public Shipment.Status getStatus() {
        return status;
    }
}
