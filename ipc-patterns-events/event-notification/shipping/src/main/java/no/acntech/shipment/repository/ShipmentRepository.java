package no.acntech.shipment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import no.acntech.shipment.model.Shipment;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

}
