package no.acntech.invoice.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import no.acntech.invoice.model.CreateInvoiceDto;
import no.acntech.order.model.OrderEvent;

@Component
public class CreateInvoiceDtoConverter implements Converter<OrderEvent, CreateInvoiceDto> {

    @Override
    public CreateInvoiceDto convert(@NonNull final OrderEvent orderEvent) {
        return CreateInvoiceDto.builder()
                .customerId(orderEvent.getCustomerId())
                .orderId(orderEvent.getOrderId())
                .build();
    }
}
