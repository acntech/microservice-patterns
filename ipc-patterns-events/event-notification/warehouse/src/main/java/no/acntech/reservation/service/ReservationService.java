package no.acntech.reservation.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.acntech.product.model.Product;
import no.acntech.product.repository.ProductRepository;
import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.Reservation;
import no.acntech.reservation.model.UpdateReservationDto;
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

    public Optional<Reservation> getReservations(@NotNull final UUID reservationId) {
        return reservationRepository.findByReservationId(reservationId);
    }

    public List<Reservation> findReservations(final UUID orderId) {
        if (orderId == null) {
            return reservationRepository.findAll();
        } else {
            return reservationRepository.findAllByOrderId(orderId);
        }
    }

    @Async
    @Transactional
    public void createReservation(@Valid final CreateReservationDto createReservation) {
        UUID orderId = createReservation.getOrderId();
        UUID productId = createReservation.getProductId();
        Long quantity = createReservation.getQuantity();

        Optional<Reservation> existingReservation = reservationRepository.findByOrderIdAndProduct_ProductId(orderId, productId);

        if (existingReservation.isPresent()) {
            Reservation reservation = existingReservation.get();

            LOGGER.error("Reservation already exists for order-id {} and product-id", orderId, productId);
            reservationEventProducer.publish(reservation.getReservationId());
        } else {
            Optional<Product> existingProduct = productRepository.findByProductId(productId);

            if (existingProduct.isPresent()) {
                Product product = existingProduct.get();

                Reservation reservation = Reservation.builder()
                        .orderId(orderId)
                        .product(product)
                        .quantity(quantity)
                        .statusConfirmed()
                        .build();
                Reservation savedReservation = reservationRepository.save(reservation);

                LOGGER.debug("Created reservation for reservation-id {}", savedReservation.getReservationId());
                reservationEventProducer.publish(savedReservation.getReservationId());
            } else {
                Reservation reservation = Reservation.builder()
                        .orderId(orderId)
                        .quantity(quantity)
                        .statusRejected()
                        .build();
                Reservation savedReservation = reservationRepository.save(reservation);

                LOGGER.error("No product found for product-id {}", productId);
                reservationEventProducer.publish(savedReservation.getReservationId());
            }
        }
    }

    @Async
    @Transactional
    public void updateReservation(@Valid final UpdateReservationDto updateReservation) {
        UUID orderId = updateReservation.getOrderId();
        UUID productId = updateReservation.getProductId();
        Long quantity = updateReservation.getQuantity();

        Optional<Reservation> existingReservation = reservationRepository.findByOrderIdAndProduct_ProductId(orderId, productId);

        if (existingReservation.isPresent()) {
            Reservation reservation = existingReservation.get();
            reservation.setQuantity(quantity);

            Reservation savedReservation = reservationRepository.save(reservation);

            LOGGER.debug("Updated reservation for reservation-id {}", savedReservation.getReservationId());
            reservationEventProducer.publish(savedReservation.getReservationId());
        } else {
            LOGGER.error("No reservation found for for order-id {} and product-id", orderId, productId);
        }
    }

    @Async
    @Transactional
    public void deleteReservation(@NotNull final UUID reservationId) {
        Optional<Reservation> existingReservation = reservationRepository.findByReservationId(reservationId);

        if (existingReservation.isPresent()) {
            Reservation reservation = existingReservation.get();

            reservationRepository.delete(reservation);

            LOGGER.debug("Deleted reservation for reservation-id {}", reservation.getReservationId());
            reservationEventProducer.publish(reservation.getReservationId());
        } else {
            LOGGER.error("No reservation found for reservation-id {}", reservationId);
            reservationEventProducer.publish(reservationId);
        }
    }
}
