package no.acntech.reservation.resource;

import brave.ScopedSpan;
import brave.Tracer;
import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.UpdateReservationDto;
import no.acntech.reservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequestMapping(path = "reservations")
@RestController
public class ReservationsResource {

    private final Tracer tracer;
    private final ReservationService reservationService;

    public ReservationsResource(final Tracer tracer,
                                final ReservationService reservationService) {
        this.tracer = tracer;
        this.reservationService = reservationService;
    }

    @GetMapping(path = "{reservationId}")
    public ResponseEntity<ReservationDto> get(@PathVariable("reservationId") final UUID reservationId) {
        ScopedSpan span = tracer.startScopedSpan("ReservationsResource#get");
        try {
            final ReservationDto reservation = reservationService.getReservation(reservationId);
            return ResponseEntity.ok(reservation);
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    @GetMapping
    public ResponseEntity<List<ReservationDto>> find(@RequestParam(name = "orderId", required = false) final UUID orderId) {
        ScopedSpan span = tracer.startScopedSpan("ReservationsResource#find");
        try {
            final List<ReservationDto> reservations = reservationService.findReservations(orderId);
            return ResponseEntity.ok(reservations);
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    @PostMapping
    public ResponseEntity<ReservationDto> create(@Valid @RequestBody final CreateReservationDto createReservation) {
        ScopedSpan span = tracer.startScopedSpan("ReservationsResource#create");
        try {
            final ReservationDto reservation = reservationService.createReservation(createReservation);
            return ResponseEntity.accepted().body(reservation);
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    @PutMapping(path = "{reservationId}")
    public ResponseEntity<ReservationDto> update(@PathVariable("reservationId") final UUID reservationId,
                                                 @Valid @RequestBody final UpdateReservationDto updateReservation) {
        ScopedSpan span = tracer.startScopedSpan("ReservationsResource#update");
        try {
            final ReservationDto reservationDto = reservationService.updateReservation(reservationId, updateReservation);
            return ResponseEntity.accepted().body(reservationDto);
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    @DeleteMapping(path = "{reservationId}")
    public ResponseEntity<ReservationDto> delete(@PathVariable("reservationId") final UUID reservationId) {
        ScopedSpan span = tracer.startScopedSpan("ReservationsResource#delete");
        try {
            final ReservationDto reservationDto = reservationService.deleteReservation(reservationId);
            return ResponseEntity.accepted().body(reservationDto);
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }
}
