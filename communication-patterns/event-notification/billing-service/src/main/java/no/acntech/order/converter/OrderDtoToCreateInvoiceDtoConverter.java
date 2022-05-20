package no.acntech.order.converter;

import no.acntech.invoice.model.CreateInvoiceDto;
import no.acntech.order.model.OrderDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoToCreateInvoiceDtoConverter implements Converter<OrderDto, CreateInvoiceDto> {

    @NonNull
    @Override
    public CreateInvoiceDto convert(@NonNull final OrderDto source) {
        return CreateInvoiceDto.builder()
                .orderId(source.getOrderId())
                .customerId(source.getCustomerId())
                .build();
    }
}
