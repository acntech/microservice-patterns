package no.acntech.inventory.service;

import no.acntech.inventory.model.Inventory;
import no.acntech.inventory.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(final InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<Inventory> findInventories(final UUID productId) {
        if (productId == null) {
            return inventoryRepository.findAll();
        } else {
            return inventoryRepository.findAllByProduct_ProductId(productId);
        }
    }
}
