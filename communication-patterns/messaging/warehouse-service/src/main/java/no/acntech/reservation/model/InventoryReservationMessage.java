package no.acntech.reservation.model;

import java.util.Map;

public class InventoryReservationMessage {

    private Long orderId;
    private Map<String, Integer> productQuantityMap;

    public Long getOrderId() {
        return orderId;
    }

    public Map<String, Integer> getProductQuantityMap() {
        return productQuantityMap;
    }
}
