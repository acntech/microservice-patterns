package no.acntech.shipment.repository;

import no.acntech.shipment.model.ShipmentEntity;
import no.acntech.shipment.model.ShipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShipmentRepository extends JpaRepository<ShipmentEntity, Long> {

    Optional<ShipmentEntity> findByShipmentId(UUID shipmentId);

    List<ShipmentEntity> findAllByCustomerId(UUID customerId);

    List<ShipmentEntity> findAllByOrderId(UUID orderId);

    List<ShipmentEntity> findAllByStatus(ShipmentStatus status);

    List<ShipmentEntity> findAllByCustomerIdAndStatus(UUID customerId, ShipmentStatus status);

    List<ShipmentEntity> findAllByOrderIdAndStatus(UUID orderId, ShipmentStatus status);

    List<ShipmentEntity> findAllByCustomerIdAndOrderIdAndStatus(UUID customerId, UUID orderId, ShipmentStatus status);
}
