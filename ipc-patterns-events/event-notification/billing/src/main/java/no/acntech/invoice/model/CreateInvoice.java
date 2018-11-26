package no.acntech.invoice.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Valid
public class CreateInvoice {

    @NotNull
    private UUID orderId;

    public UUID getOrderId() {
        return orderId;
    }
}
