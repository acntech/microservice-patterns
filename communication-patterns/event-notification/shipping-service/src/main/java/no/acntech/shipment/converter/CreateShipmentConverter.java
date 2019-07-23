package no.acntech.shipment.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import no.acntech.shipment.model.CreateShipment;
import no.acntech.shipment.model.Shipment;

@Component
public class CreateShipmentConverter implements Converter<CreateShipment, Shipment> {

    @Override
    public Shipment convert(@NonNull final CreateShipment createShipment) {
        return Shipment.builder()
                .orderId(createShipment.getOrderId())
                .build();
    }
}
