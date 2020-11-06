package no.acntech.reservation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import no.acntech.reservation.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductId(String productId);
}
