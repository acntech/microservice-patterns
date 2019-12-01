package no.acntech.reservation.service;

import brave.ScopedSpan;
import brave.Tracer;
import no.acntech.product.model.Product;
import no.acntech.product.repository.ProductRepository;
import no.acntech.reservation.exception.ReservationNotFoundException;
import no.acntech.reservation.model.*;
import no.acntech.reservation.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings({"Duplicates", "WeakerAccess"})
@Service
public class ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);

    private final Tracer tracer;
    private final ConversionService conversionService;
    private final ReservationRepository reservationRepository;
    private final ProductRepository productRepository;

    public ReservationService(final Tracer tracer,
                              final ConversionService conversionService,
                              final ReservationRepository reservationRepository,
                              final ProductRepository productRepository) {
        this.tracer = tracer;
        this.conversionService = conversionService;
        this.reservationRepository = reservationRepository;
        this.productRepository = productRepository;
    }

    public ReservationDto getReservation(@NotNull final UUID reservationId) {
        ScopedSpan span = tracer.startScopedSpan("ReservationService#getReservation");
        try {
            return reservationRepository.findByReservationId(reservationId)
                    .map(this::convert)
                    .orElseThrow(() -> new ReservationNotFoundException(reservationId));
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    public List<ReservationDto> findReservations(final UUID orderId) {
        ScopedSpan span = tracer.startScopedSpan("ReservationService#findReservations");
        try {
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
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    @Transactional
    public ReservationDto createReservation(@Valid final CreateReservationDto createReservation) {
        ScopedSpan span = tracer.startScopedSpan("ReservationService#createReservation");
        try {
            final UUID orderId = createReservation.getOrderId();
            final UUID productId = createReservation.getProductId();
            final Long quantity = createReservation.getQuantity();

            final Optional<Reservation> existingReservation = reservationRepository.findByOrderIdAndProduct_ProductId(orderId, productId);

            if (existingReservation.isPresent()) {
                final Reservation existing = existingReservation.get();
                final Reservation reservation = Reservation.builder()
                        .reservationId(existing.getReservationId())
                        .orderId(orderId)
                        .quantity(quantity)
                        .statusFailed()
                        .build();
                reservationRepository.save(reservation);

                LOGGER.error("Reservation already exists for order-id {} and product-id {}", orderId, productId);
                return convert(reservation);
            } else {
                final Optional<Product> existingProduct = productRepository.findByProductId(productId);

                if (existingProduct.isPresent()) {
                    final Product product = existingProduct.get();

                    if (product.getStock() < quantity) {
                        final Reservation reservation = Reservation.builder()
                                .orderId(orderId)
                                .product(product)
                                .quantity(quantity)
                                .statusRejected()
                                .build();
                        reservationRepository.save(reservation);

                        LOGGER.error("Product stock insufficient for product-id {}", productId);
                        return convert(reservation);
                    } else {
                        final Reservation reservation = Reservation.builder()
                                .orderId(orderId)
                                .product(product)
                                .quantity(quantity)
                                .statusReserved()
                                .build();
                        reservationRepository.save(reservation);

                        LOGGER.info("Created reservation for order-id {} and product-id {}", orderId, productId);
                        return convert(reservation);

                    }
                } else {
                    final Reservation reservation = Reservation.builder()
                            .orderId(orderId)
                            .quantity(quantity)
                            .statusFailed()
                            .build();
                    reservationRepository.save(reservation);

                    LOGGER.error("No product found for product-id {}", productId);
                    return convert(reservation);

                }
            }
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    @Transactional
    public ReservationDto updateReservation(@NotNull final UUID reservationId,
                                            @Valid final UpdateReservationDto updateReservation) {
        ScopedSpan span = tracer.startScopedSpan("ReservationService#updateReservation");
        try {
            final Long quantity = updateReservation.getQuantity();
            final ReservationStatus status = updateReservation.getStatus();

            final Optional<Reservation> existingReservation = reservationRepository.findByReservationId(reservationId);

            if (existingReservation.isPresent()) {
                final Reservation reservation = existingReservation.get();

                if (quantity != null) {
                    reservation.setQuantity(quantity);
                }
                if (status != null && canUpdateStatus(reservation.getStatus())) {
                    reservation.setStatus(status);
                }

                reservationRepository.save(reservation);

                LOGGER.info("Updated reservation for reservation-id {}", reservationId);
                return convert(reservation);
            } else {
                throw new ReservationNotFoundException(reservationId);
            }
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    @Transactional
    public ReservationDto deleteReservation(@NotNull final UUID reservationId) {
        ScopedSpan span = tracer.startScopedSpan("ReservationService#deleteReservation");
        try {
            final Optional<Reservation> existingReservation = reservationRepository.findByReservationId(reservationId);

            if (existingReservation.isPresent()) {
                final Reservation reservation = existingReservation.get();
                reservation.setStatus(ReservationStatus.CANCELED);
                reservationRepository.save(reservation);

                LOGGER.info("Updated reservation for reservation-id {}", reservationId);
                return convert(reservation);
            } else {
                throw new ReservationNotFoundException(reservationId);
            }
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    private boolean canUpdateStatus(final ReservationStatus status) {
        return status != null && status != ReservationStatus.CANCELED && status != ReservationStatus.CONFIRMED;
    }

    private ReservationDto convert(final Reservation reservation) {
        return conversionService.convert(reservation, ReservationDto.class);
    }
}
