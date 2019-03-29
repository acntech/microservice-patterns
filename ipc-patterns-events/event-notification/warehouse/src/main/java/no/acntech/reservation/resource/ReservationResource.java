package no.acntech.reservation.resource;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.acntech.reservation.model.Reservation;
import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.service.ReservationService;

@RequestMapping(path = "reservations")
@RestController
public class ReservationResource {

    private final ReservationService reservationService;

    public ReservationResource(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationDto>> find(@RequestParam(name = "orderId", required = false) final UUID orderId) {
        List<Reservation> reservations = reservationService.findReservations(orderId);
        List<ReservationDto> reservationDtos = reservations.stream()
                .map(reservation -> ReservationDto.builder()
                        .reservationId(reservation.getReservationId())
                        .orderId(reservation.getOrderId())
                        .productId(reservation.getProduct().getProductId())
                        .quantity(reservation.getQuantity())
                        .status(reservation.getStatus())
                        .created(reservation.getCreated())
                        .modified(reservation.getModified())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservationDtos);
    }
}
