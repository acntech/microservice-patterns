package no.acntech.order.integration.warehouse;

import java.util.Map;
import java.util.stream.Collectors;

import no.acntech.order.entity.Order;
import no.acntech.order.entity.Orderline;

class InventoryReservation {

    private String orderId;
    private Map<String, Integer> productQuantityMap;

    public String getOrderId() {
        return orderId;
    }

    public Map<String, Integer> getProductQuantityMap() {
        return productQuantityMap;
    }

    static InventoryReservation createFromOrder(Order order) {
        InventoryReservation inventoryReservation = new InventoryReservation();
        inventoryReservation.orderId = order.getId();
        inventoryReservation.productQuantityMap = order.getOrderlines().stream()
                .collect(Collectors.toMap(Orderline::getProductId, Orderline::getQuantity));

        return inventoryReservation;
    }
}
