package no.acntech.reservation.service;

import no.acntech.product.model.Product;
import no.acntech.product.repository.ProductRepository;
import no.acntech.reservation.model.Reservation;
import no.acntech.reservation.model.SaveReservation;
import no.acntech.reservation.producer.ReservationEventProducer;
import no.acntech.reservation.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public List<Reservation> findReservations(final UUID orderId) {
        if (orderId == null) {
            return reservationRepository.findAll();
        } else {
            return reservationRepository.findAllByOrderId(orderId);
        }
    }

    @Transactional
    public void saveReservation(final SaveReservation saveReservation) {
        UUID orderId = saveReservation.getOrderId();
        UUID productId = saveReservation.getProductId();
        Long quantity = saveReservation.getQuantity();

        Optional<Reservation> existingReservation = reservationRepository.findByOrderIdAndProduct_ProductId(orderId, productId);

        if (existingReservation.isPresent()) {
            Reservation reservation = existingReservation.get();
            reservation.setQuantity(quantity);
            reservationRepository.save(reservation);
            LOGGER.debug("Updated reservation for order-id {} and product-id {}", orderId, productId);
            reservationEventProducer.reservationUpdated(orderId, productId);
        } else {
            Optional<Product> existingProduct = productRepository.findByProductId(productId);
            if (existingProduct.isPresent()) {
                Product product = existingProduct.get();
                Reservation reservation = Reservation.builder()
                        .orderId(orderId)
                        .product(product)
                        .quantity(quantity)
                        .build();
                reservationRepository.save(reservation);
                LOGGER.debug("Created reservation for order-id {} and product-id {}", orderId, productId);
                reservationEventProducer.reservationCreated(orderId, productId);
            } else {
                LOGGER.debug("Cannot make reservation for order-id {} due to unknown product-id {}", orderId, productId);
                reservationEventProducer.productNotFound(orderId, productId);
            }
        }
    }
}
