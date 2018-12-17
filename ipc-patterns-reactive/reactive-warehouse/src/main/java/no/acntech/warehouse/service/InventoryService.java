package no.acntech.warehouse.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.acntech.warehouse.entity.Inventory;
import no.acntech.warehouse.repository.InventoryRepository;

@Service
public class InventoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryService.class);

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    //TODO if we cant find the product or if we cant reserve then we dont reserve. This should probably be handled
    public Mono<Void> reserve(InventoryReservation reservation) {
        return Flux.fromStream(reservation.getProductQuantityMap().entrySet().stream())
                .flatMap(e -> inventoryRepository.findById(e.getKey()).doOnNext(i -> i.reserve(e.getValue())))
                .flatMap(inventoryRepository::save)
                .then();

        //        reservation.getProductQuantityMap().forEach((productId, quantity) -> {
        //            inventoryRepository.findById(productId)
        //                    .flatMap(e -> Mono.just(e.reserve(quantity)));
        //
        //
        //        });
        //        reservation.getProductQuantityMap().forEach((productId, quantity) -> {
        //            Optional<Inventory> optionalProduct = inventoryRepository.findById(productId);
        //            if (!optionalProduct.isPresent()) {
        //                throw new UnknownProductException(productId);
        //            }
        //
        //            Inventory inventory = optionalProduct.get();
        //            boolean reserved = inventory.reserve(quantity);
        //            if (!reserved) {
        //                throw new OutOfStockException(productId, quantity);
        //            }
        //        });
        //
        //        LOGGER.info("Inventory reserved for order with orderId={}", reservation.getOrderId());

    }
}
