package no.acntech.shipment.converter;

import no.acntech.order.model.OrderDto;
import no.acntech.shipment.model.CreateShipmentDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoToCreateShipmentDtoConverter implements Converter<OrderDto, CreateShipmentDto> {

    @NonNull
    @Override
    public CreateShipmentDto convert(@NonNull final OrderDto source) {
        return CreateShipmentDto.builder()
                .customerId(source.getCustomerId())
                .orderId(source.getOrderId())
                .build();
    }
}
