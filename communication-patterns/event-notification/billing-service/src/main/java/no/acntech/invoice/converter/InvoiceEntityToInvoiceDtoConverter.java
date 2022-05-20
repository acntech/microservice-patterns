package no.acntech.invoice.converter;

import no.acntech.invoice.model.InvoiceDto;
import no.acntech.invoice.model.InvoiceEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class InvoiceEntityToInvoiceDtoConverter implements Converter<InvoiceEntity, InvoiceDto> {

    @NonNull
    @Override
    public InvoiceDto convert(@NonNull InvoiceEntity source) {
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
