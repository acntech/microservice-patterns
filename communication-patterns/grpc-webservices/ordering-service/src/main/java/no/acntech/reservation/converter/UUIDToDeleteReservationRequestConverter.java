package no.acntech.reservation.converter;

import com.google.protobuf.StringValue;
import no.acntech.reservation.model.DeleteReservationHeader;
import no.acntech.reservation.model.DeleteReservationRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDToDeleteReservationRequestConverter implements Converter<UUID, DeleteReservationRequest> {

    @NonNull
    @Override
    public DeleteReservationRequest convert(@NonNull final UUID source) {
        final var header = DeleteReservationHeader.newBuilder()
                .setReservationId(StringValue.of(source.toString()))
                .build();
        return DeleteReservationRequest.newBuilder()
                .setHeader(header)
                .build();
    }
}
