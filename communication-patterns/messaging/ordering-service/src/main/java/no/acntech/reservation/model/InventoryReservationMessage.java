package no.acntech.reservation.model;

import java.util.Map;

public class InventoryReservationMessage {

    private Long orderId;
    private Map<String, Integer> productQuantityMap;

    public InventoryReservationMessage(Long orderId, Map<String, Integer> productQuantityMap) {
        this.orderId = orderId;
        this.productQuantityMap = productQuantityMap;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Map<String, Integer> getProductQuantityMap() {
        return productQuantityMap;
    }
}
