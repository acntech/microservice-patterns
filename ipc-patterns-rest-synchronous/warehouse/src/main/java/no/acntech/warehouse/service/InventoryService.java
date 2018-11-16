package no.acntech.warehouse.service;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.acntech.warehouse.entity.Inventory;
import no.acntech.warehouse.repository.InventoryRepository;
import no.acntech.warehouse.service.exception.OutOfStockException;
import no.acntech.warehouse.service.exception.UnknownProductException;

@Service
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * Reduces the quantity in warehouse. If one product fail the whole
     * reservation failes.
     */
    public void reserve(InventoryReservation reservation) {
        reservation.getProductQuantityMap().forEach((productId, quantity) -> {
            Optional<Inventory> optionalProduct = inventoryRepository.findByProductId(productId);
            if (!optionalProduct.isPresent()) {
                throw new UnknownProductException(productId);
            }

            Inventory inventory = optionalProduct.get();
            boolean reserved = inventory.reserve(quantity);
            if (!reserved) {
                throw new OutOfStockException(productId, quantity);
            }
        });
    }

    public List<Inventory> findAllProducts() {
        return inventoryRepository.findAll();
    }
}
