package no.acntech.reservation.resource;

import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.PendingReservationDto;
import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.UpdateReservationDto;
import no.acntech.reservation.service.ReservationOrchestrationService;
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

import java.util.List;
import java.util.UUID;

@RequestMapping(path = "/api/reservations")
@RestController
public class ReservationsResource {

    private final ReservationOrchestrationService reservationOrchestrationService;

    public ReservationsResource(final ReservationOrchestrationService reservationOrchestrationService) {
        this.reservationOrchestrationService = reservationOrchestrationService;
    }

    @GetMapping(path = "{reservationId}")
    public ResponseEntity<ReservationDto> get(@PathVariable("reservationId") final UUID reservationId) {
        final var reservationDto = reservationOrchestrationService.getReservation(reservationId);
        return ResponseEntity.ok(reservationDto);
    }

    @GetMapping
    public ResponseEntity<List<ReservationDto>> find(@RequestParam(name = "orderId", required = false) final UUID orderId) {
        final var reservationDtos = reservationOrchestrationService.findReservations(orderId);
        return ResponseEntity.ok(reservationDtos);
    }

    @PostMapping
    public ResponseEntity<PendingReservationDto> create(@RequestBody final CreateReservationDto createReservation) {
        final var pendingReservationDto = PendingReservationDto.initWithReservationId();
        reservationOrchestrationService.createReservation(pendingReservationDto.getReservationId(), createReservation);
        return ResponseEntity.accepted()
                .body(pendingReservationDto);
    }

    @PutMapping(path = "{reservationId}")
    public ResponseEntity<Void> update(@PathVariable("reservationId") final UUID reservationId,
                                       @RequestBody final UpdateReservationDto updateReservation) {
        reservationOrchestrationService.updateReservation(reservationId, updateReservation);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping(path = "{reservationId}")
    public ResponseEntity<Void> delete(@PathVariable("reservationId") final UUID reservationId) {
        reservationOrchestrationService.deleteReservation(reservationId);
        return ResponseEntity.accepted().build();
    }
}
