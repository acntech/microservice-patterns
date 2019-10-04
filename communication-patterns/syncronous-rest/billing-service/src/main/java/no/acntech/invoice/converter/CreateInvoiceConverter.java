package no.acntech.invoice.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import no.acntech.invoice.model.CreateInvoice;
import no.acntech.invoice.model.Invoice;

@Component
public class CreateInvoiceConverter implements Converter<CreateInvoice, Invoice> {

    @Override
    public Invoice convert(@NonNull final CreateInvoice createInvoice) {
        return Invoice.builder()
                .orderId(createInvoice.getOrderId())
                .customerId(createInvoice.getCustomerId())
                .build();
    }
}
