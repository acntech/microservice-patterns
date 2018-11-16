package no.acntech.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;

import no.acntech.inventory.model.Inventory;
import no.acntech.inventory.repository.InventoryRepository;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(final InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<Inventory> findInventories() {
        return inventoryRepository.findAll();
    }
}
