package no.acntech.warehouse.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import no.acntech.warehouse.entity.Inventory;

public interface InventoryRepository extends ReactiveMongoRepository<Inventory, String> {

}
