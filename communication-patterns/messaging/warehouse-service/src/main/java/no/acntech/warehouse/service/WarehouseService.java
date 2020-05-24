package no.acntech.warehouse.service;

import javax.transaction.Transactional;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.acntech.messaging.types.warehouse.InventoryReservationMessage;
import no.acntech.warehouse.entity.Inventory;
import no.acntech.messaging.types.warehouse.InventoryReservationConfirmationMessage;
import no.acntech.warehouse.messaging.producers.ReservationConfirmationProducer;
import no.acntech.warehouse.repository.InventoryRepository;
import no.acntech.warehouse.service.exception.OutOfStockException;
import no.acntech.warehouse.service.exception.UnknownProductException;

@Service
@Transactional
public class WarehouseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseService.class);

    private final ReservationConfirmationProducer reservationConfirmationProducer;
    private final InventoryRepository inventoryRepository;

    @Autowired
    public WarehouseService(ReservationConfirmationProducer reservationConfirmationProducer,
                            InventoryRepository inventoryRepository) {
        this.reservationConfirmationProducer = reservationConfirmationProducer;
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * Reduces the quantity in warehouse. Sends a message with the status of
     * the reservation asynchronously.
     */
    public void reserveAndConfirm(InventoryReservationMessage reservation) {
        InventoryReservationConfirmationMessage confirmation;
        try {
            reserve(reservation);
            LOGGER.info("Reservation ok for orderId={}", reservation.getOrderId());
            confirmation = InventoryReservationConfirmationMessage.confirmed(reservation.getOrderId());
        } catch (Exception e) {
            LOGGER.info("Reservation failed for orderId={}, errorMessage={}", reservation.getOrderId(), e.getMessage());
            confirmation = InventoryReservationConfirmationMessage.error(reservation.getOrderId(), e.getMessage());
        }

        reservationConfirmationProducer.sendConfirmation(confirmation);
    }

    private void reserve(InventoryReservationMessage reservation) {
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
}
