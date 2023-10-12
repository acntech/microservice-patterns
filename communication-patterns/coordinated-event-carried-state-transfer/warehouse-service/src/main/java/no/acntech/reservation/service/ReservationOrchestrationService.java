package no.acntech.reservation.service;

import no.acntech.reservation.model.CancelReservationDto;
import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.ReservationEvent;
import no.acntech.reservation.model.ReservationQuery;
import no.acntech.reservation.model.UpdateReservationDto;
import no.acntech.reservation.producer.ReservationEventProducer;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@SuppressWarnings({"Duplicates"})
@Service
public class ReservationOrchestrationService {

    private final ConversionService conversionService;
    private final ReservationService reservationService;
    private final ReservationEventProducer reservationEventProducer;

    public ReservationOrchestrationService(final ConversionService conversionService,
                                           final ReservationService reservationService,
                                           final ReservationEventProducer reservationEventProducer) {
        this.conversionService = conversionService;
        this.reservationService = reservationService;
        this.reservationEventProducer = reservationEventProducer;
    }

    public ReservationDto getReservation(@NotNull final UUID reservationId) {
        return reservationService.getReservation(reservationId);
    }

    public List<ReservationDto> findReservations(@NotNull @Valid final ReservationQuery reservationQuery) {
        return reservationService.findReservations(reservationQuery);
    }

    public void createReservation(@NotNull final CreateReservationDto createReservationDto) {
        final var reservationDto = reservationService.createReservation(createReservationDto);
        final var reservationEvent = convert(reservationDto);
        reservationEventProducer.publish(reservationEvent);
    }

    public void updateAllReservations(@NotNull final UpdateReservationDto updateReservationDto) {
        final var reservationDtos = reservationService.updateAllReservations(updateReservationDto);
        reservationDtos.forEach(reservationDto -> {
            final var reservationEvent = convert(reservationDto);
            reservationEventProducer.publish(reservationEvent);
        });
    }

    public void updateReservation(@NotNull final UpdateReservationDto updateReservationDto) {
        final var reservationDto = reservationService.updateReservation(updateReservationDto);
        final var reservationEvent = convert(reservationDto);
        reservationEventProducer.publish(reservationEvent);
    }

    public void cancelAllReservations(@NotNull final CancelReservationDto cancelReservationDto) {
        final var reservationDtos = reservationService.cancelAllReservations(cancelReservationDto);
        reservationDtos.forEach(reservationDto -> {
            final var reservationEvent = convert(reservationDto);
            reservationEventProducer.publish(reservationEvent);
        });
    }

    public void cancelReservation(@NotNull final CancelReservationDto cancelReservationDto) {
        final var reservationDto = reservationService.cancelReservation(cancelReservationDto);
        final var reservationEvent = convert(reservationDto);
        reservationEventProducer.publish(reservationEvent);
    }

    private ReservationEvent convert(final ReservationDto reservationDto) {
        final var reservationEvent = conversionService.convert(reservationDto, ReservationEvent.class);
        Assert.notNull(reservationEvent, "Failed to convert ReservationDto to ReservationEvent");
        return reservationEvent;
    }
}
