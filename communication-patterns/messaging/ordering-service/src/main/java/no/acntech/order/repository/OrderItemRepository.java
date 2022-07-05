package no.acntech.order.repository;

import no.acntech.order.model.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {

    Optional<OrderItemEntity> findByItemId(UUID itemId);

    Optional<OrderItemEntity> findByParent_OrderIdAndProductId(UUID orderId, UUID productId);
}
