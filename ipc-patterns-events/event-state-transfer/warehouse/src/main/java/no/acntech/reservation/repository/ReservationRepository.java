package no.acntech.reservation.repository;

import no.acntech.reservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByOrderId(UUID orderId);

    Optional<Reservation> findByOrderIdAndProduct_ProductId(UUID orderId, UUID productId);
}
