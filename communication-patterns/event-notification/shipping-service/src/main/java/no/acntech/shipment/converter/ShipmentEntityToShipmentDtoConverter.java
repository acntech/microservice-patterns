package no.acntech.shipment.converter;

import no.acntech.shipment.model.ShipmentDto;
import no.acntech.shipment.model.ShipmentEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ShipmentEntityToShipmentDtoConverter implements Converter<ShipmentEntity, ShipmentDto> {

    @NonNull
    @Override
    public ShipmentDto convert(@NonNull final ShipmentEntity shipment) {
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
