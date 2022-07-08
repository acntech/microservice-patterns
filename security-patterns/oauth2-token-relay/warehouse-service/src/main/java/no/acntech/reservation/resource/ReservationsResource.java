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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
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
    public ResponseEntity create(@Valid @RequestBody final CreateReservationDto createReservation) {
        ReservationDto reservation = reservationService.createReservation(createReservation);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .pathSegment(reservation.getReservationId().toString())
                .build()
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(path = "{reservationId}")
    public ResponseEntity<ReservationDto> update(@PathVariable("reservationId") UUID reservationId,
                                                 @Valid @RequestBody final UpdateReservationDto updateReservation) {
        ReservationDto reservation = reservationService.updateReservation(reservationId, updateReservation);
        return ResponseEntity.ok(reservation);
    }

    @DeleteMapping(path = "{reservationId}")
    public ResponseEntity delete(@PathVariable("reservationId") UUID reservationId) {
        reservationService.deleteReservation(reservationId);
        return ResponseEntity.ok().build();
    }
}
