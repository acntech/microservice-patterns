package no.acntech.order.repository;

import no.acntech.order.model.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {

    Optional<OrderLine> findByOrderIdAndProductId(Long orderId, UUID productId);
}
