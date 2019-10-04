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
import no.acntech.reservation.service.ReservationFacadeService;

@RequestMapping(path = "reservations")
@RestController
public class ReservationsResource {

    private final ReservationFacadeService reservationFacadeService;

    public ReservationsResource(final ReservationFacadeService reservationFacadeService) {
        this.reservationFacadeService = reservationFacadeService;
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<ReservationDto> get(@PathVariable("id") UUID reservationId) {
        final ReservationDto reservation = reservationFacadeService.getReservation(reservationId);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping
    public ResponseEntity<List<ReservationDto>> find(@RequestParam(name = "orderId", required = false) final UUID orderId) {
        final List<ReservationDto> reservations = reservationFacadeService.findReservations(orderId);
        return ResponseEntity.ok(reservations);
    }

    @PostMapping
    public ResponseEntity<PendingReservationDto> create(@Valid @RequestBody final CreateReservationDto createReservation) {
        final PendingReservationDto pendingReservation = PendingReservationDto.builder().build();
        reservationFacadeService.createReservation(pendingReservation, createReservation);
        return ResponseEntity.accepted()
                .body(pendingReservation);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity update(@PathVariable("id") UUID reservationId,
                                 @Valid @RequestBody final UpdateReservationDto updateReservation) {
        reservationFacadeService.updateReservation(reservationId, updateReservation);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity delete(@PathVariable("id") UUID reservationId) {
        reservationFacadeService.deleteReservation(reservationId);
        return ResponseEntity.accepted().build();
    }
}
