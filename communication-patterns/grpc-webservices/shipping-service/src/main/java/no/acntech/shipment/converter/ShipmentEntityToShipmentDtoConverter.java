package no.acntech.shipment.converter;

import com.google.protobuf.StringValue;
import com.google.protobuf.Timestamp;
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
        return ShipmentDto.newBuilder()
                .setShipmentId(StringValue.of(source.getShipmentId().toString()))
                .setCustomerId(StringValue.of(source.getCustomerId().toString()))
                .setOrderId(StringValue.of(source.getOrderId().toString()))
                .setStatus(source.getStatus())
                .setCreated(Timestamp.newBuilder()
                        .setSeconds(source.getCreated().toInstant().getEpochSecond())
                        .setNanos(source.getCreated().toInstant().getNano())
                        .build())
                .setModified(source.getModified() != null ? Timestamp.newBuilder()
                        .setSeconds(source.getModified().toInstant().getEpochSecond())
                        .setNanos(source.getModified().toInstant().getNano())
                        .build() : Timestamp.getDefaultInstance())
                .build();
    }
}
