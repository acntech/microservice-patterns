package no.acntech.reservation.service;

import no.acntech.reservation.exception.ReservationNotFoundException;
import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.UpdateReservationDto;
import no.acntech.reservation.producer.ReservationEventProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("Duplicates")
@Validated
@Service
public class ReservationOrchestrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationOrchestrationService.class);
    private final ReservationService reservationService;
    private final ReservationEventProducer reservationEventProducer;

    public ReservationOrchestrationService(final ReservationService reservationService,
                                           final ReservationEventProducer reservationEventProducer) {
        this.reservationService = reservationService;
        this.reservationEventProducer = reservationEventProducer;
    }

    public ReservationDto getReservation(final UUID reservationId) {
        return reservationService.getReservation(reservationId);
    }

    public List<ReservationDto> findReservations(final UUID orderId) {
        return reservationService.findReservations(orderId);
    }

    @Async
    public void createReservation(@NotNull final UUID reservationId,
                                  @NotNull @Valid final CreateReservationDto createReservation) {
        reservationService.createReservation(reservationId, createReservation);
        reservationEventProducer.publish(reservationId);
    }

    @Async
    public void updateReservation(@NotNull final UUID reservationId,
                                  @NotNull @Valid final UpdateReservationDto updateReservation) {
        try {
            reservationService.updateReservation(reservationId, updateReservation);
            reservationEventProducer.publish(reservationId);
        } catch (ReservationNotFoundException e) {
            LOGGER.error("An error occurred during processing of update to reservation", e);
        }
    }

    @Async
    public void deleteReservation(@NotNull final UUID reservationId) {
        try {
            reservationService.deleteReservation(reservationId);
            reservationEventProducer.publish(reservationId);
        } catch (ReservationNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
