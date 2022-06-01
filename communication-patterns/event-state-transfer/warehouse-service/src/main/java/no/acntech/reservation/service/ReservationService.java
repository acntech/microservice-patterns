package no.acntech.reservation.service;

import no.acntech.product.exception.ProductNotFoundException;
import no.acntech.product.model.ProductEntity;
import no.acntech.product.repository.ProductRepository;
import no.acntech.reservation.exception.ReservationNotFoundException;
import no.acntech.reservation.model.*;
import no.acntech.reservation.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings({"Duplicates"})
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
        return reservationRepository.findByReservationId(reservationId)
                .map(this::convert)
                .orElseThrow(() -> new ReservationNotFoundException("No reservation found for reservation-id " + reservationId));
    }

    public List<ReservationDto> findReservations(@NotNull @Valid final ReservationQuery reservationQuery) {
        if (reservationQuery.getOrderId() == null) {
            return reservationRepository.findAll(SORT_BY_ID)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else {
            return reservationRepository.findAllByOrderId(reservationQuery.getOrderId(), SORT_BY_ID)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        }
    }

    public ReservationDto createReservation(@NotNull @Valid final CreateReservationDto createReservationDto) {
        try {
            final var productEntity = productRepository.findByProductId(createReservationDto.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(createReservationDto.getProductId()));
            if (productEntity.getStock() >= createReservationDto.getQuantity()) {
                return createReservationReserved(createReservationDto, productEntity);
            } else {
                return createReservationRejected(createReservationDto, productEntity);
            }
        } catch (ProductNotFoundException e) {
            LOGGER.debug("Reservation failed", e);
            return createReservationFailed(createReservationDto);
        }
    }

    private ReservationDto createReservationReserved(@NotNull @Valid final CreateReservationDto createReservationDto,
                                                     @NotNull @Valid final ProductEntity productEntity) {
        final var reservationEntity = ReservationEntity.builder()
                .statusReserved()
                .orderId(createReservationDto.getOrderId())
                .product(productEntity)
                .quantity(createReservationDto.getQuantity())
                .build();
        final var savedReservationEntity = reservationRepository.save(reservationEntity);
        return convert(savedReservationEntity);
    }

    private ReservationDto createReservationRejected(@NotNull @Valid final CreateReservationDto createReservationDto,
                                                     @NotNull @Valid final ProductEntity productEntity) {
        final var reservationEntity = ReservationEntity.builder()
                .statusRejected()
                .orderId(createReservationDto.getOrderId())
                .product(productEntity)
                .quantity(createReservationDto.getQuantity())
                .build();
        final var savedReservationEntity = reservationRepository.save(reservationEntity);
        return convert(savedReservationEntity);
    }

    private ReservationDto createReservationFailed(@NotNull @Valid final CreateReservationDto createReservationDto) {
        final var reservationEntity = ReservationEntity.builder()
                .statusFailed()
                .orderId(createReservationDto.getOrderId())
                .quantity(createReservationDto.getQuantity())
                .build();
        final var savedReservationEntity = reservationRepository.save(reservationEntity);
        return convert(savedReservationEntity);
    }

    public List<ReservationDto> updateAllReservations(@NotNull final UpdateReservationDto updateReservationDto) {
        final var reservationEntities = reservationRepository
                .findAllByOrderId(updateReservationDto.getOrderId(), SORT_BY_ID);

        return reservationEntities.stream()
                .map(reservationEntity -> {
                    reservationEntity.confirmReservation();
                    return reservationRepository.save(reservationEntity);
                })
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public ReservationDto updateReservation(@NotNull final UpdateReservationDto updateReservationDto) {
        final var reservationEntity = reservationRepository
                .findByOrderIdAndProduct_ProductId(updateReservationDto.getOrderId(), updateReservationDto.getProductId())
                .orElseThrow(() -> new ReservationNotFoundException("No reservation found for order-id " + updateReservationDto.getOrderId() + " and product-id" + updateReservationDto.getProductId()));
        reservationEntity.confirmReservation();
        final var savedReservationEntity = reservationRepository.save(reservationEntity);
        return convert(savedReservationEntity);
    }

    public List<ReservationDto> cancelAllReservations(@NotNull final CancelReservationDto cancelReservationDto) {
        final var reservationEntities = reservationRepository
                .findAllByOrderId(cancelReservationDto.getOrderId(), SORT_BY_ID);

        return reservationEntities.stream().map(reservationEntity -> {
                    reservationEntity.cancelReservation();
                    return reservationRepository.save(reservationEntity);
                })
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public ReservationDto cancelReservation(@NotNull final CancelReservationDto cancelReservationDto) {
        final var reservationEntity = reservationRepository
                .findByOrderIdAndProduct_ProductId(cancelReservationDto.getOrderId(), cancelReservationDto.getProductId())
                .orElseThrow(() -> new ReservationNotFoundException("No reservation found for order-id " + cancelReservationDto.getOrderId() + " and product-id" + cancelReservationDto.getProductId()));
        reservationEntity.cancelReservation();
        final var savedReservationEntity = reservationRepository.save(reservationEntity);
        return convert(savedReservationEntity);
    }

    private ReservationDto convert(final ReservationEntity reservation) {
        final var reservationDto = conversionService.convert(reservation, ReservationDto.class);
        Assert.notNull(reservationDto, "Failed to convert ReservationEntity to ReservationDto");
        return reservationDto;
    }
}
