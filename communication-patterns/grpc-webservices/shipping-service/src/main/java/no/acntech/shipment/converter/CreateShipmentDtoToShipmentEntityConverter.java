package no.acntech.shipment.converter;

import no.acntech.shipment.model.CreateShipmentDto;
import no.acntech.shipment.model.ShipmentEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateShipmentDtoToShipmentEntityConverter implements Converter<CreateShipmentDto, ShipmentEntity> {

    @NonNull
    @Override
    public ShipmentEntity convert(@NonNull final CreateShipmentDto source) {
        return ShipmentEntity.builder()
                .customerId(UUID.fromString(source.getCustomerId().getValue()))
                .orderId(UUID.fromString(source.getOrderId().getValue()))
                .build();
    }
}
