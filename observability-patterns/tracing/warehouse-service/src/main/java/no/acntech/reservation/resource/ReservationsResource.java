package no.acntech.reservation.resource;

import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.UpdateReservationDto;
import no.acntech.reservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping(path = "/api/reservations")
@RestController
public class ReservationsResource {

    private final ReservationService reservationService;

    public ReservationsResource(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping(path = "{reservationId}")
    public ResponseEntity<ReservationDto> get(@PathVariable("reservationId") final UUID reservationId) {
        final var reservationDto = reservationService.getReservation(reservationId);
        return ResponseEntity.ok(reservationDto);
    }

    @GetMapping
    public ResponseEntity<List<ReservationDto>> find(@RequestParam(name = "orderId", required = false) final UUID orderId) {
        final var reservationDtos = reservationService.findReservations(orderId);
        return ResponseEntity.ok(reservationDtos);
    }

    @PostMapping
    public ResponseEntity<ReservationDto> create(@RequestBody final CreateReservationDto createReservation) {
        final var reservationDto = reservationService.createReservation(createReservation);
        return ResponseEntity.ok(reservationDto);
    }

    @PutMapping(path = "{reservationId}")
    public ResponseEntity<ReservationDto> update(@PathVariable("reservationId") final UUID reservationId,
                                                 @RequestBody final UpdateReservationDto updateReservation) {
        final var reservationDto = reservationService.updateReservation(reservationId, updateReservation);
        return ResponseEntity.ok(reservationDto);
    }

    @DeleteMapping(path = "{reservationId}")
    public ResponseEntity<ReservationDto> delete(@PathVariable("reservationId") final UUID reservationId) {
        final var reservationDto = reservationService.deleteReservation(reservationId);
        return ResponseEntity.ok(reservationDto);
    }
}
