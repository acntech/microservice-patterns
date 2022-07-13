package no.acntech.reservation.service;

import no.acntech.product.repository.ProductRepository;
import no.acntech.reservation.exception.ReservationNotFoundException;
import no.acntech.reservation.model.ReservationEntity;
import no.acntech.reservation.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings({"Duplicates", "WeakerAccess"})
@Validated
@Service
public class ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);
    private final ConversionService conversionService;
    private final ReservationRepository reservationRepository;
    private final ProductRepository productRepository;

    public ReservationService(final ConversionService conversionService,
                              final ReservationRepository reservationRepository,
                              final ProductRepository productRepository) {
        this.conversionService = conversionService;
        this.reservationRepository = reservationRepository;
        this.productRepository = productRepository;
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

    @Transactional
    public void createReservation(@NotNull final UUID reservationId,
                                  @Valid final CreateReservationDto createReservation) {
        final var optionalReservationEntity = reservationRepository
                .findByOrderIdAndProduct_ProductId(createReservation.getOrderId(), createReservation.getProductId());

        if (optionalReservationEntity.isPresent()) {
            ReservationEntity reservation = ReservationEntity.builder()
                    .reservationId(reservationId)
                    .orderId(createReservation.getOrderId())
                    .quantity(createReservation.getQuantity())
                    .statusFailed()
                    .build();
            reservationRepository.save(reservation);
            LOGGER.error("Reservation already exists for order-id {} and product-id {}", createReservation.getOrderId(), createReservation.getProductId());
        } else {
            final var optionalProductEntity = productRepository.findByProductId(createReservation.getProductId());

            if (optionalProductEntity.isPresent()) {
                final var productEntity = optionalProductEntity.get();

                if (productEntity.getStock() < createReservation.getQuantity()) {
                    final var reservationEntity = ReservationEntity.builder()
                            .reservationId(reservationId)
                            .orderId(createReservation.getOrderId())
                            .quantity(createReservation.getQuantity())
                            .product(productEntity)
                            .statusRejected()
                            .build();
                    reservationRepository.save(reservationEntity);

                    LOGGER.error("Product stock insufficient for reservation-id {}", reservationId);
                } else {
                    final var reservationEntity = ReservationEntity.builder()
                            .reservationId(reservationId)
                            .orderId(createReservation.getOrderId())
                            .quantity(createReservation.getQuantity())
                            .product(productEntity)
                            .statusReserved()
                            .build();
                    reservationRepository.save(reservationEntity);

                    LOGGER.info("Created reservation for reservation-id {}", reservationId);
                }
            } else {
                final var reservationEntity = ReservationEntity.builder()
                        .reservationId(reservationId)
                        .orderId(createReservation.getOrderId())
                        .quantity(createReservation.getQuantity())
                        .statusFailed()
                        .build();
                reservationRepository.save(reservationEntity);

                LOGGER.error("No product found for reservation-id {}", reservationId);
            }
        }
    }

    @Transactional
    public void updateReservation(@NotNull final UUID reservationId,
                                  @Valid final UpdateReservationDto updateReservation) {
        final var reservationEntity = reservationRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        if (updateReservation.getQuantity() != null) {
            reservationEntity.setQuantity(updateReservation.getQuantity());
        }
        if (updateReservation.getStatus() != null && reservationEntity.canUpdateStatus()) {
            reservationEntity.setStatus(updateReservation.getStatus());
        }

        reservationRepository.save(reservationEntity);

        LOGGER.info("Updated reservation for reservation-id {}", reservationId);
    }

    @Transactional
    public void deleteReservation(@NotNull final UUID reservationId) {
        final var reservationEntity = reservationRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        reservationEntity.setStatus(ReservationStatus.CANCELED);
        reservationRepository.save(reservationEntity);

        LOGGER.info("Updated reservation for reservation-id {}", reservationId);
    }

    private ReservationDto convert(final ReservationEntity reservation) {
        return conversionService.convert(reservation, ReservationDto.class);
    }
}
