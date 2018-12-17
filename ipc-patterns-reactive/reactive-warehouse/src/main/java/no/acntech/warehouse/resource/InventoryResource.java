package no.acntech.warehouse.resource;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import no.acntech.warehouse.entity.Inventory;
import no.acntech.warehouse.entity.NewInventory;
import no.acntech.warehouse.repository.InventoryRepository;

@RestController
@RequestMapping("inventories")
public class InventoryResource {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryResource(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Inventory> inventories() {
        return inventoryRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Inventory> addInventory(@RequestBody @Valid NewInventory newInventory) {
        return Mono.just(newInventory)
                .map(n -> Inventory.builer().name(n.getName()).quantity(n.getQuantity()).build())
                .flatMap(inventoryRepository::save);
    }
}
