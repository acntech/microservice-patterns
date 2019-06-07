package no.acntech.shipment.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import no.acntech.shipment.model.Shipment;
import no.acntech.shipment.model.ShipmentDto;

@Component
public class ShipmentDtoConverter implements Converter<Shipment, ShipmentDto> {

    @Override
    public ShipmentDto convert(@NonNull final Shipment shipment) {
        return ShipmentDto.builder()
                .shipmentId(shipment.getShipmentId())
                .customerId(shipment.getCustomerId())
                .orderId(shipment.getOrderId())
                .status(shipment.getStatus())
                .created(shipment.getCreated())
                .modified(shipment.getModified())
                .build();
    }
}
