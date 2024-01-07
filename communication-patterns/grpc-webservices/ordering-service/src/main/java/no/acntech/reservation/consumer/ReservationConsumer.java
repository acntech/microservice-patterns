package no.acntech.reservation.consumer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import net.devh.boot.grpc.client.inject.GrpcClient;
import no.acntech.reservation.endpoint.ReservationEndpointGrpc;
import no.acntech.reservation.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.UUID;

@SuppressWarnings("Duplicates")
@Component
public class ReservationConsumer {

    @Autowired
    private ConversionService conversionService;
    @GrpcClient("warehouse-service")
    private ReservationEndpointGrpc.ReservationEndpointBlockingStub blockingStub;

    public ReservationDto createReservation(@Valid final CreateReservationDto createReservationDto) {
        final var request = conversionService.convert(createReservationDto, CreateReservationRequest.class);
        final var response = blockingStub.createReservation(request);
        return response.getBody();
    }

    public ReservationDto updateReservation(@NotNull final UUID reservationId,
                                            @Valid final UpdateReservationDto updateReservationDto) {
        final var request = conversionService.convert(Pair.of(reservationId, updateReservationDto), UpdateReservationRequest.class);
        final var response = blockingStub.updateReservation(request);
        return response.getBody();
    }

    public ReservationDto deleteReservation(@NotNull final UUID reservationId) {
        final var request = conversionService.convert(reservationId, DeleteReservationRequest.class);
        final var response = blockingStub.deleteReservation(request);
        return response.getBody();
    }
}
