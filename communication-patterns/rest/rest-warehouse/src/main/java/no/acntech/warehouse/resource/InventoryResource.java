package no.acntech.warehouse.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.acntech.warehouse.entity.Inventory;
import no.acntech.warehouse.repository.InventoryRepository;

@RestController
@RequestMapping("inventories")
public class InventoryResource {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryResource(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @GetMapping
    public ResponseEntity<List<Inventory>> inventories() {
        return ResponseEntity.ok(inventoryRepository.findAll());
    }
}
