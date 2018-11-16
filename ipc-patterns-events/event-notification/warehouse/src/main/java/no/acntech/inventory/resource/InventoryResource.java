package no.acntech.inventory.resource;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.acntech.inventory.model.Inventory;
import no.acntech.inventory.service.InventoryService;

@RequestMapping(path = "inventories")
@RestController
public class InventoryResource {

    private final InventoryService inventoryService;

    public InventoryResource(final InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public List<Inventory> get() {
        return inventoryService.findInventories();
    }
}
