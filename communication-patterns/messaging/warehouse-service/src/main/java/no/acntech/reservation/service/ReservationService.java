package no.acntech.reservation.service;

import javax.transaction.Transactional;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.acntech.reservation.exception.OutOfStockException;
import no.acntech.reservation.exception.UnknownProductException;
import no.acntech.reservation.model.Inventory;
import no.acntech.reservation.model.InventoryReservationConfirmationMessage;
import no.acntech.reservation.model.InventoryReservationMessage;
import no.acntech.reservation.producer.ReservationConfirmationProducer;
import no.acntech.reservation.repository.InventoryRepository;

@Service
@Transactional
public class ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationConfirmationProducer reservationConfirmationProducer;
    private final InventoryRepository inventoryRepository;

    @Autowired
    public ReservationService(ReservationConfirmationProducer reservationConfirmationProducer,
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
