package no.acntech.invoice.model;

import java.util.UUID;

public class InvoiceQuery {

    private UUID orderId;
    private InvoiceStatus status;

    public UUID getOrderId() {
        return orderId;
    }

    public InvoiceStatus getStatus() {
        return status;
    }
}
