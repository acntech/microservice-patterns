package no.acntech.reservation.resource;

import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.UpdateReservationDto;
import no.acntech.reservation.service.ReservationService;
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
    public ResponseEntity<ReservationDto> create(@Valid @RequestBody final CreateReservationDto createReservation) {
        final ReservationDto reservation = reservationService.createReservation(createReservation);
        return ResponseEntity.accepted().body(reservation);
    }

    @PutMapping(path = "{reservationId}")
    public ResponseEntity<ReservationDto> update(@PathVariable("reservationId") final UUID reservationId,
                                                 @Valid @RequestBody final UpdateReservationDto updateReservation) {
        final ReservationDto reservationDto = reservationService.updateReservation(reservationId, updateReservation);
        return ResponseEntity.accepted().body(reservationDto);
    }

    @DeleteMapping(path = "{reservationId}")
    public ResponseEntity<ReservationDto> delete(@PathVariable("reservationId") final UUID reservationId) {
        final ReservationDto reservationDto = reservationService.deleteReservation(reservationId);
        return ResponseEntity.accepted().body(reservationDto);
    }
}
