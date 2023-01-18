package no.acntech.reservation.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.acntech.product.exception.ProductNotFoundException;
import no.acntech.product.repository.ProductRepository;
import no.acntech.reservation.exception.ReservationAlreadyExistsException;
import no.acntech.reservation.exception.ReservationNotFoundException;
import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.ReservationEntity;
import no.acntech.reservation.model.UpdateReservationDto;
import no.acntech.reservation.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

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
        LOGGER.debug("Getting reservation for ID {}", reservationId);
        return reservationRepository.findByReservationId(reservationId)
                .map(this::convert)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));
    }

    public List<ReservationDto> findReservations(final UUID orderId) {
        LOGGER.debug("Finding reservations");
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
    public ReservationDto createReservation(@NotNull @Valid final CreateReservationDto createReservationDto) {
        LOGGER.debug("Create reservation");
        reservationRepository
                .findByOrderIdAndProduct_ProductId(createReservationDto.getOrderId(), createReservationDto.getProductId())
                .ifPresent(reservationEntity -> {
                    throw new ReservationAlreadyExistsException(createReservationDto.getOrderId(), createReservationDto.getProductId());
                });

        final var productEntity = productRepository.findByProductId(createReservationDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(createReservationDto.getProductId()));

        if (productEntity.getStock() < createReservationDto.getQuantity()) {
            final var reservationEntity = ReservationEntity.builder()
                    .orderId(createReservationDto.getOrderId())
                    .quantity(createReservationDto.getQuantity())
                    .product(productEntity)
                    .statusRejected()
                    .build();
            final var savedReservationEntity = reservationRepository.save(reservationEntity);
            LOGGER.error("Product stock insufficient for reservation-id {}", savedReservationEntity.getReservationId());
            return convert(savedReservationEntity);
        } else {
            final var reservationEntity = ReservationEntity.builder()
                    .orderId(createReservationDto.getOrderId())
                    .quantity(createReservationDto.getQuantity())
                    .product(productEntity)
                    .statusReserved()
                    .build();
            final var savedReservationEntity = reservationRepository.save(reservationEntity);
            LOGGER.info("Created reservation for reservation-id {}", savedReservationEntity.getReservationId());
            return convert(savedReservationEntity);
        }
    }

    @Transactional
    public ReservationDto updateReservation(@NotNull final UUID reservationId,
                                            @NotNull @Valid final UpdateReservationDto updateReservation) {
        LOGGER.debug("Update reservation for ID {}", reservationId);
        final var reservationEntity = reservationRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));
        if (updateReservation.getQuantity() != null) {
            reservationEntity.setQuantity(updateReservation.getQuantity());
        }
        if (updateReservation.getStatus() != null && reservationEntity.canUpdateStatus()) {
            reservationEntity.setStatus(updateReservation.getStatus());
        }
        final var savedReservationEntity = reservationRepository.save(reservationEntity);
        LOGGER.info("Updated reservation for reservation-id {}", reservationId);
        return convert(savedReservationEntity);
    }

    @Transactional
    public ReservationDto deleteReservation(@NotNull final UUID reservationId) {
        LOGGER.debug("Deleting reservation for ID {}", reservationId);
        final var reservationEntity = reservationRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));
        reservationEntity.cancelReservation();
        final var savedReservationEntity = reservationRepository.save(reservationEntity);
        LOGGER.info("Updated reservation for reservation-id {}", reservationId);
        return convert(savedReservationEntity);
    }

    private ReservationDto convert(final ReservationEntity reservation) {
        return conversionService.convert(reservation, ReservationDto.class);
    }
}
