package no.acntech.reservation.resource;

import no.acntech.reservation.model.Reservation;
import no.acntech.reservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequestMapping(path = "reservations")
@RestController
public class ReservationResource {

    private final ReservationService reservationService;

    public ReservationResource(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> find(@RequestParam(name = "orderId", required = false) final UUID orderId) {
        return ResponseEntity.ok(reservationService.findReservations(orderId));
    }
}
