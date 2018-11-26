package no.acntech.shipment.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Valid
public class CreateShipment {

    @NotNull
    private UUID orderId;

    public UUID getOrderId() {
        return orderId;
    }
}
