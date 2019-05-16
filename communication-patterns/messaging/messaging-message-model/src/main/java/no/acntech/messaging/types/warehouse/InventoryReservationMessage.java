package no.acntech.messaging.types.warehouse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryReservationMessage {

    private Long orderId;
    private Map<String, Integer> productQuantityMap;
}
