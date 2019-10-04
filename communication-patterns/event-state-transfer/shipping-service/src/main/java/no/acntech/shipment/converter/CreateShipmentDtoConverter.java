package no.acntech.shipment.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import no.acntech.order.model.OrderEvent;
import no.acntech.shipment.model.CreateShipmentDto;

@Component
public class CreateShipmentDtoConverter implements Converter<OrderEvent, CreateShipmentDto> {

    @Override
    public CreateShipmentDto convert(@NonNull final OrderEvent orderEvent) {
        return CreateShipmentDto.builder()
                .customerId(orderEvent.getCustomerId())
                .orderId(orderEvent.getOrderId())
                .build();
    }
}
