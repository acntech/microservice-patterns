package no.acntech.invoice.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import no.acntech.invoice.model.CreateInvoiceDto;
import no.acntech.invoice.model.Invoice;

@Component
public class CreateInvoiceDtoConverter implements Converter<CreateInvoiceDto, Invoice> {

    @Override
    public Invoice convert(@NonNull final CreateInvoiceDto createInvoice) {
        return Invoice.builder()
                .customerId(createInvoice.getCustomerId())
                .orderId(createInvoice.getOrderId())
                .build();
    }
}
