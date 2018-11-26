package no.acntech.invoice.converter;

import no.acntech.invoice.model.CreateInvoice;
import no.acntech.invoice.model.Invoice;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Component
public class CreateInvoiceConverter implements Converter<CreateInvoice, Invoice> {

    @Override
    public Invoice convert(@Valid @NotNull final CreateInvoice createInvoice) {
        return Invoice.builder()
                .orderId(createInvoice.getOrderId())
                .build();
    }
}
