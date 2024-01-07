package no.acntech.invoice.converter;

import no.acntech.invoice.model.FindInvoicesRequest;
import no.acntech.invoice.model.InvoiceQuery;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FindInvoicesRequestToInvoiceQueryConverter implements Converter<FindInvoicesRequest, InvoiceQuery> {

    @NonNull
    @Override
    public InvoiceQuery convert(@NonNull final FindInvoicesRequest source) {
        return InvoiceQuery.builder()
                .customerId(source.getHeader().hasCustomerId() ? UUID.fromString(source.getHeader().getCustomerId().getValue()) : null)
                .orderId(source.getHeader().hasOrderId() ? UUID.fromString(source.getHeader().getOrderId().getValue()) : null)
                .status(source.getHeader().hasStatus() ? source.getHeader().getStatus() : null)
                .build();
    }
}
