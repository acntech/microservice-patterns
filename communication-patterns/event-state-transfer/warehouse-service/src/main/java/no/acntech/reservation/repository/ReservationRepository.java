package no.acntech.reservation.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import no.acntech.reservation.model.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByReservationId(UUID reservationId);

    List<Reservation> findAllByOrderId(UUID orderId, Sort sort);

    Optional<Reservation> findByOrderIdAndProduct_ProductId(UUID orderId, UUID productId);
}
