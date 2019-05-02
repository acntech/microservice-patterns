package no.acntech.reservation.service;

import no.acntech.product.model.Product;
import no.acntech.product.repository.ProductRepository;
import no.acntech.reservation.exception.ReservationNotFoundException;
import no.acntech.reservation.model.*;
import no.acntech.reservation.producer.ReservationEventProducer;
import no.acntech.reservation.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
@Service
public class ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);
    private final ConversionService conversionService;
    private final ReservationRepository reservationRepository;
    private final ProductRepository productRepository;
    private final ReservationEventProducer reservationEventProducer;

    public ReservationService(final ConversionService conversionService,
                              final ReservationRepository reservationRepository,
                              final ProductRepository productRepository,
                              final ReservationEventProducer reservationEventProducer) {
        this.conversionService = conversionService;
        this.reservationRepository = reservationRepository;
        this.productRepository = productRepository;
        this.reservationEventProducer = reservationEventProducer;
    }

    public ReservationDto getReservation(@NotNull final UUID reservationId) {
        return reservationRepository.findByReservationId(reservationId)
                .map(this::convert)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));
    }

    public List<ReservationDto> findReservations(final UUID orderId) {
        if (orderId == null) {
            return reservationRepository.findAll()
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else {
            return reservationRepository.findAllByOrderId(orderId)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        }
    }

    @Async
    @Transactional
    public void createReservation(@Valid final PendingReservationDto pendingReservation,
                                  @Valid final CreateReservationDto createReservation) {
        final UUID reservationId = pendingReservation.getReservationId();
        final UUID orderId = createReservation.getOrderId();
        final UUID productId = createReservation.getProductId();
        final Long quantity = createReservation.getQuantity();

        final Optional<Reservation> existingReservation = reservationRepository.findByOrderIdAndProduct_ProductId(orderId, productId);

        if (existingReservation.isPresent()) {
            final Reservation reservation = Reservation.builder()
                    .reservationId(reservationId)
                    .orderId(orderId)
                    .quantity(quantity)
                    .statusFailed()
                    .build();
            reservationRepository.save(reservation);

            LOGGER.error("Reservation already exists for order-id {} and product-id {}", orderId, productId);
            reservationEventProducer.publish(reservationId);
        } else {
            final Optional<Product> existingProduct = productRepository.findByProductId(productId);

            if (existingProduct.isPresent()) {
                Product product = existingProduct.get();

                if (product.getStock() < quantity) {
                    final Reservation reservation = Reservation.builder()
                            .reservationId(reservationId)
                            .orderId(orderId)
                            .product(product)
                            .quantity(quantity)
                            .statusRejected()
                            .build();
                    reservationRepository.save(reservation);

                    LOGGER.error("Product stock insufficient for reservation-id {}", reservationId);
                } else {
                    final Reservation reservation = Reservation.builder()
                            .reservationId(reservationId)
                            .orderId(orderId)
                            .product(product)
                            .quantity(quantity)
                            .statusReserved()
                            .build();
                    reservationRepository.save(reservation);

                    LOGGER.info("Created reservation for reservation-id {}", reservationId);
                }
            } else {
                final Reservation reservation = Reservation.builder()
                        .reservationId(reservationId)
                        .orderId(orderId)
                        .quantity(quantity)
                        .statusFailed()
                        .build();
                reservationRepository.save(reservation);

                LOGGER.error("No product found for reservation-id {}", reservationId);
            }

            reservationEventProducer.publish(reservationId);
        }
    }

    @Async
    @Transactional
    public void updateReservation(@NotNull final UUID reservationId,
                                  @Valid final UpdateReservationDto updateReservation) {
        final Long quantity = updateReservation.getQuantity();

        final Optional<Reservation> existingReservation = reservationRepository.findByReservationId(reservationId);

        if (existingReservation.isPresent()) {
            final Reservation reservation = existingReservation.get();
            reservation.setQuantity(quantity);
            reservationRepository.save(reservation);

            LOGGER.info("Updated reservation for reservation-id {}", reservationId);
            reservationEventProducer.publish(reservationId);
        } else {
            LOGGER.error("No reservation found for for reservation-id {}", reservationId);
        }
    }

    @Async
    @Transactional
    public void deleteReservation(@NotNull final UUID reservationId) {
        Optional<Reservation> existingReservation = reservationRepository.findByReservationId(reservationId);

        if (existingReservation.isPresent()) {
            final Reservation reservation = existingReservation.get();
            reservation.setStatus(ReservationStatus.CANCELED);
            reservationRepository.save(reservation);

            LOGGER.info("Updated reservation for reservation-id {}", reservationId);
            reservationEventProducer.publish(reservationId);
        } else {
            LOGGER.error("No reservations found for reservation-id {}", reservationId);
        }
    }

    private ReservationDto convert(final Reservation reservation) {
        return conversionService.convert(reservation, ReservationDto.class);
    }
}
