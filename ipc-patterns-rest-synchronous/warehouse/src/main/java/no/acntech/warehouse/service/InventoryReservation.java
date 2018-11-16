package no.acntech.warehouse.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InventoryReservation {

    private String customerId;
    private Map<String, Integer> productQuantityMap;
}
