package no.acntech.reservation.service;

import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.acntech.product.exception.ProductNotFoundException;
import no.acntech.product.model.Product;
import no.acntech.product.repository.ProductRepository;
import no.acntech.reservation.model.Reservation;
import no.acntech.reservation.model.SaveReservation;
import no.acntech.reservation.producer.ReservationEventProducer;
import no.acntech.reservation.repository.ReservationRepository;

@SuppressWarnings("Duplicates")
@Service
public class ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);
    private final ReservationRepository reservationRepository;
    private final ProductRepository productRepository;
    private final ReservationEventProducer reservationEventProducer;

    public ReservationService(final ReservationRepository reservationRepository,
                              final ProductRepository productRepository,
                              final ReservationEventProducer reservationEventProducer) {
        this.reservationRepository = reservationRepository;
        this.productRepository = productRepository;
        this.reservationEventProducer = reservationEventProducer;
    }

    public List<Reservation> findReservations(@NotNull final UUID orderId) {
        if (orderId == null) {
            return reservationRepository.findAll();
        } else {
            return reservationRepository.findAllByOrderId(orderId);
        }
    }

    @Transactional
    public void saveReservation(@NotNull final SaveReservation saveReservation) {
        UUID orderId = saveReservation.getOrderId();
        UUID productId = saveReservation.getProductId();
        Long quantity = saveReservation.getQuantity();

        Optional<Reservation> existingReservation = reservationRepository.findByOrderIdAndProduct_ProductId(orderId, productId);

        if (existingReservation.isPresent()) {
            Reservation reservation = existingReservation.get();
            reservation.setQuantity(quantity);

            Reservation updatedReservation = reservationRepository.save(reservation);

            LOGGER.debug("Updated reservation for reservation-id {}", updatedReservation.getReservationId());
            reservationEventProducer.publish(updatedReservation.getReservationId());
        } else {
            Optional<Product> existingProduct = productRepository.findByProductId(productId);
            if (existingProduct.isPresent()) {
                Product product = existingProduct.get();
                Reservation reservation = Reservation.builder()
                        .orderId(orderId)
                        .product(product)
                        .quantity(quantity)
                        .build();
                Reservation savedReservation = reservationRepository.save(reservation);

                LOGGER.debug("Created reservation for reservation-id {}", savedReservation.getReservationId());
                reservationEventProducer.publish(savedReservation.getReservationId());
            } else {
                throw new ProductNotFoundException(productId);
            }
        }
    }
}
