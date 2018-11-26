package no.acntech.inventory.resource;

import no.acntech.inventory.model.Inventory;
import no.acntech.inventory.service.InventoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequestMapping(path = "inventories")
@RestController
public class InventoryResource {

    private final InventoryService inventoryService;

    public InventoryResource(final InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public List<Inventory> find() {
        return inventoryService.findInventories();
    }

    @GetMapping(path = "{productId}")
    public Inventory get(@PathVariable("productId") final UUID productId) {
        return inventoryService.getInventory(productId);
    }
}
