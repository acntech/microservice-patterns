package no.acntech.invoice.converter;

import no.acntech.invoice.model.CreateInvoiceDto;
import no.acntech.order.model.OrderDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoToCreateInvoiceConverter implements Converter<OrderDto, CreateInvoiceDto> {

    @NonNull
    @Override
    public CreateInvoiceDto convert(@NonNull final OrderDto source) {
        return CreateInvoiceDto.newBuilder()
                .setCustomerId(source.getCustomerId())
                .setOrderId(source.getOrderId())
                .build();
    }
}
