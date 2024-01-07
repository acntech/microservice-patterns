package no.acntech.invoice.converter;

import no.acntech.invoice.model.CreateInvoiceDto;
import no.acntech.invoice.model.InvoiceEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateInvoiceDtoToInvoiceEntityConverter implements Converter<CreateInvoiceDto, InvoiceEntity> {

    @NonNull
    @Override
    public InvoiceEntity convert(@NonNull final CreateInvoiceDto source) {
        return InvoiceEntity.builder()
                .customerId(UUID.fromString(source.getCustomerId().getValue()))
                .orderId(UUID.fromString(source.getOrderId().getValue()))
                .build();
    }
}
