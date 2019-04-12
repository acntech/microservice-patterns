package no.acntech.inventory.resource;

import no.acntech.inventory.model.Inventory;
import no.acntech.inventory.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequestMapping(path = "inventory")
@RestController
public class InventoryResource {

    private final InventoryService inventoryService;

    public InventoryResource(final InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<Inventory>> find(@RequestParam(name = "productId", required = false) final UUID productId) {
        return ResponseEntity.ok(inventoryService.findInventories(productId));
    }
}
