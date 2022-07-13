package no.acntech.reservation.repository;

import no.acntech.reservation.model.ReservationEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    Optional<ReservationEntity> findByReservationId(UUID reservationId);

    List<ReservationEntity> findAllByOrderId(UUID orderId, Sort sort);

    Optional<ReservationEntity> findByOrderIdAndProduct_ProductId(UUID orderId, UUID productId);
}
