package no.acntech.invoice.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import no.acntech.invoice.model.Invoice;
import no.acntech.invoice.model.InvoiceDto;

@Component
public class InvoiceDtoConverter implements Converter<Invoice, InvoiceDto> {

    @Override
    public InvoiceDto convert(@NonNull Invoice source) {
        return InvoiceDto.builder()
                .invoiceId(source.getInvoiceId())
                .customerId(source.getCustomerId())
                .orderId(source.getOrderId())
                .status(source.getStatus())
                .created(source.getCreated())
                .modified(source.getModified())
                .build();
    }
}
