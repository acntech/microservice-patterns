package no.acntech.warehouse.service;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.Map;

public class InventoryReservation {

    @NotNull
    private String orderId;
    @NotEmpty
    private Map<String, Integer> productQuantityMap;

    public String getOrderId() {
        return orderId;
    }

    public Map<String, Integer> getProductQuantityMap() {
        return productQuantityMap;
    }

    public static builder builder() {
        return new builder();
    }

    public static final class builder {

        private String orderId;
        private Map<String, Integer> productQuantityMap;

        private builder() {
        }

        public builder orderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public builder productQuantityMap(Map<String, Integer> productQuantityMap) {
            this.productQuantityMap = productQuantityMap;
            return this;
        }

        public InventoryReservation build() {
            InventoryReservation inventoryReservation = new InventoryReservation();
            inventoryReservation.productQuantityMap = this.productQuantityMap;
            inventoryReservation.orderId = this.orderId;
            return inventoryReservation;
        }
    }
}
