package no.acntech.order.integration;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.stream.Collectors;

import no.acntech.order.entity.Order;
import no.acntech.order.entity.Orderline;

@Data
@Getter
@ToString
class InventoryReservation {

    private String customerId;
    private Map<String, Integer> productQuantityMap;

    static InventoryReservation createFromOrder(Order order) {
        InventoryReservation inventoryReservation = new InventoryReservation();
        inventoryReservation.customerId = order.getCustomerId();
        inventoryReservation.productQuantityMap = order.getOrderlines().stream()
                .collect(Collectors.toMap(Orderline::getProductId, Orderline::getQuantity));

        return inventoryReservation;
    }
}
