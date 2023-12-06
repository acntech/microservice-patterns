package no.acntech.order.converter;

import no.acntech.invoice.model.CreateInvoiceDto;
import no.acntech.order.model.OrderEvent;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class OrderEventToCreateInvoiceDtoConverter implements Converter<OrderEvent, CreateInvoiceDto> {

    @NonNull
    @Override
    public CreateInvoiceDto convert(@NonNull final OrderEvent orderEvent) {
        return CreateInvoiceDto.builder()
                .customerId(orderEvent.getCustomerId())
                .orderId(orderEvent.getOrderId())
                .build();
    }
}
