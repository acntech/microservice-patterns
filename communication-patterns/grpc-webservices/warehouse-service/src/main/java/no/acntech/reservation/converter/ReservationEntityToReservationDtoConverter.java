package no.acntech.reservation.converter;

import com.google.protobuf.StringValue;
import com.google.protobuf.Timestamp;
import com.google.protobuf.UInt32Value;
import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.ReservationEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ReservationEntityToReservationDtoConverter implements Converter<ReservationEntity, ReservationDto> {

    @NonNull
    @Override
    public ReservationDto convert(@NonNull final ReservationEntity source) {
        final var builder = ReservationDto.newBuilder()
                .setReservationId(StringValue.of(source.getReservationId().toString()))
                .setOrderId(StringValue.of(source.getOrderId().toString()))
                .setProductId(StringValue.of(source.getProduct().getProductId().toString()))
                .setQuantity(UInt32Value.of(source.getQuantity()))
                .setStatus(source.getStatus())
                .setCreated(Timestamp.newBuilder()
                        .setSeconds(source.getCreated().toInstant().getEpochSecond())
                        .setNanos(source.getCreated().toInstant().getNano())
                        .build());
        if (source.getModified() != null) {
            builder
                    .setModified(Timestamp.newBuilder()
                            .setSeconds(source.getModified().toInstant().getEpochSecond())
                            .setNanos(source.getModified().toInstant().getNano())
                            .build());
        }
        return builder.build();
    }
}
