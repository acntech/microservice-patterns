package no.acntech.order.converter;

import no.acntech.order.model.OrderEvent;
import no.acntech.shipment.model.CreateShipmentDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class OrderEventToCreateShipmentDtoConverter implements Converter<OrderEvent, CreateShipmentDto> {

    @NonNull
    @Override
    public CreateShipmentDto convert(@NonNull final OrderEvent orderEvent) {
        return CreateShipmentDto.builder()
                .customerId(orderEvent.getCustomerId())
                .orderId(orderEvent.getOrderId())
                .build();
    }
}
