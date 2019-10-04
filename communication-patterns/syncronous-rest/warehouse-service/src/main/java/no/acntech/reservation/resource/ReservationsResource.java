package no.acntech.reservation.resource;

import javax.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.PendingReservationDto;
import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.UpdateReservationDto;
import no.acntech.reservation.service.ReservationService;

@RequestMapping(path = "reservations")
@RestController
public class ReservationsResource {

    private final ReservationService reservationService;

    public ReservationsResource(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping(path = "{reservationId}")
    public ResponseEntity<ReservationDto> get(@PathVariable("reservationId") final UUID reservationId) {
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
        reservationService.createReservation(createReservation);
        return ResponseEntity.accepted() //TODO må antagelig skrive om bruker av dette endepunktet elns. blir ikke riktig dette
                .body(pendingReservation);
    }

    @PutMapping(path = "{reservationId}")
    public ResponseEntity update(@PathVariable("reservationId") final UUID reservationId,
                                 @Valid @RequestBody final UpdateReservationDto updateReservation) {
        reservationService.updateReservation(reservationId, updateReservation);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping(path = "{reservationId}")
    public ResponseEntity delete(@PathVariable("reservationId") final UUID reservationId) {
        reservationService.deleteReservation(reservationId);
        return ResponseEntity.accepted().build();
    }
}
