package no.acntech.shipment.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import no.acntech.shipment.model.Shipment;
import no.acntech.shipment.model.ShipmentStatus;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    Optional<Shipment> findByShipmentId(UUID shipmentId);

    List<Shipment> findAllByCustomerId(UUID customerId);

    List<Shipment> findAllByOrderId(UUID orderId);

    List<Shipment> findAllByStatus(ShipmentStatus status);

    List<Shipment> findAllByCustomerIdAndStatus(UUID customerId, ShipmentStatus status);

    List<Shipment> findAllByOrderIdAndStatus(UUID orderId, ShipmentStatus status);

    List<Shipment> findAllByCustomerIdAndOrderIdAndStatus(UUID customerId, UUID orderId, ShipmentStatus status);
}
