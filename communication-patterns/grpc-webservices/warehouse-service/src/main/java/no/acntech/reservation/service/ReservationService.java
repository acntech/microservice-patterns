package no.acntech.reservation.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.acntech.product.exception.ProductNotFoundException;
import no.acntech.product.repository.ProductRepository;
import no.acntech.reservation.exception.ReservationAlreadyExistsException;
import no.acntech.reservation.exception.ReservationNotFoundException;
import no.acntech.reservation.model.*;
import no.acntech.reservation.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
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
    private static final Sort SORT_BY_ID = Sort.by("id");
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
        LOGGER.debug("Get reservation {}", reservationId);
        return reservationRepository.findByReservationId(reservationId)
                .map(this::convert)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));
    }

    public List<ReservationDto> findReservations(final ReservationQuery query) {
        if (query.getOrderId() != null) {
            LOGGER.debug("Find reservations by order {}", query.getOrderId());
            return reservationRepository.findAllByOrderId(query.getOrderId(), SORT_BY_ID)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else {
            LOGGER.debug("Find all reservations");
            return reservationRepository.findAll(SORT_BY_ID)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public ReservationDto createReservation(@NotNull @Valid final CreateReservationDto createReservationDto) {
        LOGGER.debug("Create reservation for order {} and product {}",
                createReservationDto.getOrderId().getValue(),
                createReservationDto.getProductId().getValue());
        final var orderId = UUID.fromString(createReservationDto.getOrderId().getValue());
        final var productId = UUID.fromString(createReservationDto.getProductId().getValue());
        final var quantity = createReservationDto.getQuantity().getValue();
        reservationRepository
                .findByOrderIdAndProduct_ProductId(orderId, productId)
                .ifPresent(reservationEntity -> {
                    throw new ReservationAlreadyExistsException(orderId, productId);
                });

        final var productEntity = productRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if (productEntity.getStock() < quantity) {
            final var reservationEntity = ReservationEntity.builder()
                    .orderId(orderId)
                    .quantity(quantity)
                    .product(productEntity)
                    .statusRejected()
                    .build();
            final var savedReservationEntity = reservationRepository.save(reservationEntity);
            LOGGER.error("Product stock insufficient for reservation {}", savedReservationEntity.getReservationId());
            return convert(savedReservationEntity);
        } else {
            final var reservationEntity = ReservationEntity.builder()
                    .orderId(orderId)
                    .quantity(quantity)
                    .product(productEntity)
                    .statusReserved()
                    .build();
            final var savedReservationEntity = reservationRepository.save(reservationEntity);
            LOGGER.info("Created reservation {}", savedReservationEntity.getReservationId());
            return convert(savedReservationEntity);
        }
    }

    @Transactional
    public ReservationDto updateReservation(@NotNull final UUID reservationId,
                                            @NotNull @Valid final UpdateReservationDto updateReservation) {
        LOGGER.debug("Update reservation {}", reservationId);
        final var reservationEntity = reservationRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));
        if (updateReservation.getQuantity() != null) {
            reservationEntity.setQuantity(updateReservation.getQuantity().getValue());
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
        LOGGER.debug("Delete reservation {}", reservationId);
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
