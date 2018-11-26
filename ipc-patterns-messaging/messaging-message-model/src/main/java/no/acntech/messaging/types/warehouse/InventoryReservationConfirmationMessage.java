package no.acntech.messaging.types.warehouse;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class InventoryReservationConfirmationMessage {

    private Long orderId;
    private String reservationId;
    private boolean reserved;
    private String errorMessage;

    private InventoryReservationConfirmationMessage(Long orderId, boolean reserved, String errorMessage) {
        this.orderId = orderId;
        this.reserved = reserved;
        this.errorMessage = errorMessage;
        this.reservationId = UUID.randomUUID().toString();
    }

    public static InventoryReservationConfirmationMessage error(Long orderId, String errorMessage) {
        return new InventoryReservationConfirmationMessage(orderId, false, errorMessage);
    }

    public static InventoryReservationConfirmationMessage confirmed(Long orderId) {
        return new InventoryReservationConfirmationMessage(orderId, true, null);
    }
}
