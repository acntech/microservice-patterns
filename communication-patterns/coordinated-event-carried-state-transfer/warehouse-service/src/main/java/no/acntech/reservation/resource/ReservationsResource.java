package no.acntech.reservation.resource;

import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.ReservationQuery;
import no.acntech.reservation.service.ReservationOrchestrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequestMapping(path = "/api/reservations")
@RestController
public class ReservationsResource {

    private final ReservationOrchestrationService reservationOrchestrationService;

    public ReservationsResource(final ReservationOrchestrationService reservationOrchestrationService) {
        this.reservationOrchestrationService = reservationOrchestrationService;
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<ReservationDto> get(@PathVariable("id") UUID reservationId) {
        final ReservationDto reservation = reservationOrchestrationService.getReservation(reservationId);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping
    public ResponseEntity<List<ReservationDto>> find(final ReservationQuery reservationQuery) {
        final List<ReservationDto> reservations = reservationOrchestrationService.findReservations(reservationQuery);
        return ResponseEntity.ok(reservations);
    }
}
