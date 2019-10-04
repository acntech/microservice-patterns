package no.acntech.shipment.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import no.acntech.shipment.model.CreateShipmentDto;
import no.acntech.shipment.model.Shipment;

@Component
public class ShipmentConverter implements Converter<CreateShipmentDto, Shipment> {

    @Override
    public Shipment convert(@NonNull final CreateShipmentDto createShipment) {
        return Shipment.builder()
                .customerId(createShipment.getCustomerId())
                .orderId(createShipment.getOrderId())
                .build();
    }
}
