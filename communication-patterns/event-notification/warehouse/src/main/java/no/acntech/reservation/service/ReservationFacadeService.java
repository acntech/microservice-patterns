package no.acntech.reservation.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import no.acntech.reservation.exception.ReservationNotFoundException;
import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.PendingReservationDto;
import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.UpdateReservationDto;
import no.acntech.reservation.producer.ReservationEventProducer;

@SuppressWarnings("Duplicates")
@Service
public class ReservationFacadeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationFacadeService.class);
    private final ReservationService reservationService;
    private final ReservationEventProducer reservationEventProducer;

    public ReservationFacadeService(final ReservationService reservationService,
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
    public void createReservation(@Valid final PendingReservationDto pendingReservation,
                                  @Valid final CreateReservationDto createReservation) {
        reservationService.createReservation(pendingReservation, createReservation);
        reservationEventProducer.publish(pendingReservation.getReservationId());
    }

    @Async
    public void updateReservation(@NotNull final UUID reservationId,
                                  @Valid final UpdateReservationDto updateReservation) {
        try {
            reservationService.updateReservation(reservationId, updateReservation);
            reservationEventProducer.publish(reservationId);
        } catch (ReservationNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
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
