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
    public ShipmentDto convert(@NonNull final ShipmentEntity source) {
        return ShipmentDto.builder()
                .shipmentId(source.getShipmentId())
                .customerId(source.getCustomerId())
                .orderId(source.getOrderId())
                .status(source.getStatus())
                .created(source.getCreated())
                .modified(source.getModified())
                .build();
    }
}
