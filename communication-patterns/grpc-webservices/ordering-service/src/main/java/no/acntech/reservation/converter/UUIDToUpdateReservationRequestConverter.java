package no.acntech.reservation.converter;

import com.google.protobuf.StringValue;
import no.acntech.reservation.model.UpdateReservationDto;
import no.acntech.reservation.model.UpdateReservationHeader;
import no.acntech.reservation.model.UpdateReservationRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDToUpdateReservationRequestConverter implements Converter<Pair<UUID, UpdateReservationDto>, UpdateReservationRequest> {

    @NonNull
    @Override
    public UpdateReservationRequest convert(@NonNull final Pair<UUID, UpdateReservationDto> source) {
        final var header = UpdateReservationHeader.newBuilder()
                .setReservationId(StringValue.of(source.getFirst().toString()))
                .build();
        return UpdateReservationRequest.newBuilder()
                .setHeader(header)
                .setBody(source.getSecond())
                .build();
    }
}
