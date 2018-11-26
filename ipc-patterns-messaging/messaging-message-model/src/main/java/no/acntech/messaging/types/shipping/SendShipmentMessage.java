package no.acntech.messaging.types.shipping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendShipmentMessage {

    private Long orderId;
    private String reservationId;
}
