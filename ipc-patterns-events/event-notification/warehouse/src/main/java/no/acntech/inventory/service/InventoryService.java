package no.acntech.inventory.service;

import no.acntech.inventory.exception.InventoryQuantityNotSufficientException;
import no.acntech.inventory.model.Inventory;
import no.acntech.inventory.model.UpdateInventory;
import no.acntech.inventory.repository.InventoryRepository;
import no.acntech.product.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(final InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<Inventory> findInventories() {
        return inventoryRepository.findAll();
    }

    public Inventory getInventory(final UUID productId) {
        return inventoryRepository.findByProduct_ProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    @Transactional
    public Inventory updateInventory(UpdateInventory updateInventory) {
        UUID productId = updateInventory.getProductId();
        Long changeQuantity = updateInventory.getQuantity();

        Inventory inventory = inventoryRepository.findByProduct_ProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Long existingQuantity = inventory.getQuantity();
        long updatedQuantity = existingQuantity + changeQuantity;

        // Check if change in quantity is more than available
        if (updatedQuantity < 0) {
            throw new InventoryQuantityNotSufficientException(productId);
        }

        inventory.setQuantity(updatedQuantity);
        return inventoryRepository.save(inventory);
    }
}
