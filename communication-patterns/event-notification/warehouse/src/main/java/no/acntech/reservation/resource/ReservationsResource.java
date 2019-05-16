package no.acntech.reservation.resource;

import no.acntech.reservation.model.*;
import no.acntech.reservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequestMapping(path = "reservations")
@RestController
public class ReservationsResource {

    private final ReservationService reservationService;

    public ReservationsResource(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping(path = "{reservationId}")
    public ResponseEntity<ReservationDto> get(@PathVariable("reservationId") UUID reservationId) {
        final ReservationDto reservation = reservationService.getReservation(reservationId);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping
    public ResponseEntity<List<ReservationDto>> find(@RequestParam(name = "orderId", required = false) final UUID orderId) {
        final List<ReservationDto> reservations = reservationService.findReservations(orderId);
        return ResponseEntity.ok(reservations);
    }

    @PostMapping
    public ResponseEntity<PendingReservationDto> create(@Valid @RequestBody final CreateReservationDto createReservation) {
        final PendingReservationDto pendingReservation = PendingReservationDto.builder().build();
        reservationService.createReservation(pendingReservation, createReservation);
        return ResponseEntity.accepted()
                .body(pendingReservation);
    }

    @PutMapping(path = "{reservationId}")
    public ResponseEntity update(@PathVariable("reservationId") UUID reservationId,
                                 @Valid @RequestBody final UpdateReservationDto updateReservation) {
        reservationService.updateReservation(reservationId, updateReservation);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping(path = "{reservationId}")
    public ResponseEntity delete(@PathVariable("reservationId") UUID reservationId) {
        reservationService.deleteReservation(reservationId);
        return ResponseEntity.accepted().build();
    }
}
