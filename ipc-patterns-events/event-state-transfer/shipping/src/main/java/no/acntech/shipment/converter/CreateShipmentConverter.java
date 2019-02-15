package no.acntech.shipment.converter;

import no.acntech.shipment.model.CreateShipment;
import no.acntech.shipment.model.Shipment;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Component
public class CreateShipmentConverter implements Converter<CreateShipment, Shipment> {

    @Override
    public Shipment convert(@Valid @NotNull final CreateShipment createShipment) {
        return Shipment.builder()
                .orderId(createShipment.getOrderId())
                .build();
    }
}
