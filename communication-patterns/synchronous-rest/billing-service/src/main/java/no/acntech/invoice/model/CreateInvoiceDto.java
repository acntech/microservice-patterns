package no.acntech.invoice.model;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class CreateInvoiceDto {

    @NotNull
    private UUID customerId;
    @NotNull
    private UUID orderId;

    public UUID getCustomerId() {
        return customerId;
    }

    public UUID getOrderId() {
        return orderId;
    }
}
