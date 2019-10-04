package no.acntech.order.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import no.acntech.order.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByItemId(UUID itemId);

    Optional<Item> findByOrderIdAndProductId(Long orderId, UUID productId);

    Optional<Item> findByReservationId(UUID reservationId);
}
