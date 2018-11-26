package no.acntech.invoice.model;

import java.util.UUID;

public class InvoiceQuery {

    private UUID orderId;
    private Invoice.Status status;

    public UUID getOrderId() {
        return orderId;
    }

    public Invoice.Status getStatus() {
        return status;
    }
}
