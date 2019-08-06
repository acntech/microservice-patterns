package no.acntech.shipment.repository;

import no.acntech.shipment.model.Shipment;
import no.acntech.shipment.model.ShipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    Optional<Shipment> findByShipmentId(UUID shipmentId);

    List<Shipment> findAllByOrderId(UUID orderId);

    List<Shipment> findAllByStatus(ShipmentStatus status);

    List<Shipment> findAllByOrderIdAndStatus(UUID orderId, ShipmentStatus status);
}