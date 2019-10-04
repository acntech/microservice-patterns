package no.acntech.reservation.resource;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.ReservationQuery;
import no.acntech.reservation.service.ReservationService;

@RequestMapping(path = "reservations")
@RestController
public class ReservationsResource {

    private final ReservationService reservationService;

    public ReservationsResource(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<ReservationDto> get(@PathVariable("id") UUID reservationId) {
        final ReservationDto reservation = reservationService.getReservation(reservationId);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping
    public ResponseEntity<List<ReservationDto>> find(final ReservationQuery reservationQuery) {
        final List<ReservationDto> reservations = reservationService.findReservations(reservationQuery);
        return ResponseEntity.ok(reservations);
    }
}
