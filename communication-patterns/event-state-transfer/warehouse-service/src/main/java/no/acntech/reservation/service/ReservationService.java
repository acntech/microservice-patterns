package no.acntech.reservation.service;

import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import no.acntech.reservation.exception.ReservationNotFoundException;
import no.acntech.reservation.model.Reservation;
import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.repository.ReservationRepository;

@SuppressWarnings({"Duplicates"})
@Service
public class ReservationService {

    private final ConversionService conversionService;
    private final ReservationRepository reservationRepository;

    public ReservationService(final ConversionService conversionService,
                              final ReservationRepository reservationRepository) {
        this.conversionService = conversionService;
        this.reservationRepository = reservationRepository;
    }

    public ReservationDto getReservation(@NotNull final UUID reservationId) {
        return reservationRepository.findByReservationId(reservationId)
                .map(this::convert)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));
    }

    public List<ReservationDto> findReservations(final UUID orderId) {
        if (orderId == null) {
            return reservationRepository.findAll()
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else {
            return reservationRepository.findAllByOrderId(orderId)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        }
    }

    private ReservationDto convert(final Reservation reservation) {
        return conversionService.convert(reservation, ReservationDto.class);
    }
}
