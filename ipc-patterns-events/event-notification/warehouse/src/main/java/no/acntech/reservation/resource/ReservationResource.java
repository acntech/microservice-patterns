package no.acntech.reservation.resource;

import javax.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.convert.ConversionService;
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
import no.acntech.reservation.model.Reservation;
import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.UpdateReservationDto;
import no.acntech.reservation.service.ReservationService;

@RequestMapping(path = "reservations")
@RestController
public class ReservationResource {

    private final ConversionService conversionService;
    private final ReservationService reservationService;

    public ReservationResource(final ConversionService conversionService,
                               final ReservationService reservationService) {
        this.conversionService = conversionService;
        this.reservationService = reservationService;
    }

    @GetMapping(path = "{reservationId}")
    public ResponseEntity<ReservationDto> get(@PathVariable("reservationId") UUID reservationId) {
        Optional<Reservation> reservations = reservationService.getReservations(reservationId);
        return reservations
                .map(this::convert)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.noContent()::build);
    }

    @GetMapping
    public ResponseEntity<List<ReservationDto>> find(@RequestParam(name = "orderId", required = false) final UUID orderId) {
        List<Reservation> reservations = reservationService.findReservations(orderId);
        List<ReservationDto> reservationDtos = reservations.stream()
                .map(this::convert)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservationDtos);
    }

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody final CreateReservationDto createReservation) {
        reservationService.createReservation(createReservation);
        return ResponseEntity.accepted().build();
    }

    @PutMapping
    public ResponseEntity update(@Valid @RequestBody final UpdateReservationDto updateReservation) {
        reservationService.updateReservation(updateReservation);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping(path = "{reservationId}")
    public ResponseEntity delete(@PathVariable("reservationId") UUID reservationId) {
        reservationService.deleteReservation(reservationId);
        return ResponseEntity.accepted().build();
    }

    private ReservationDto convert(final Reservation reservation) {
        return conversionService.convert(reservation, ReservationDto.class);
    }
}
