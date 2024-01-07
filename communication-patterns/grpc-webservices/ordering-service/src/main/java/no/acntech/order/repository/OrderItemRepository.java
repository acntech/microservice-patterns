package no.acntech.order.repository;

import no.acntech.order.model.OrderItemEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {

    Optional<OrderItemEntity> findByItemId(UUID itemId);

    List<OrderItemEntity> findAllByParentOrderId(UUID orderId, Sort sort);
}
