package no.acntech.order.repository;

import no.acntech.order.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByOrderIdAndProductId(Long orderId, UUID productId);
}
