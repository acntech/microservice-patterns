package no.acntech.reservation.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.acntech.product.exception.ProductNotAvailableException;
import no.acntech.product.exception.ProductStockInsufficientException;
import no.acntech.product.model.Product;
import no.acntech.product.repository.ProductRepository;
import no.acntech.reservation.exception.ReservationAlreadyExistsException;
import no.acntech.reservation.exception.ReservationNotFoundException;
import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.Reservation;
import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.ReservationStatus;
import no.acntech.reservation.model.UpdateReservationDto;
import no.acntech.reservation.repository.ReservationRepository;

@SuppressWarnings({"Duplicates"})
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
    public ReservationDto createReservation(@Valid final CreateReservationDto createReservation) {
        UUID orderId = createReservation.getOrderId();
        UUID productId = createReservation.getProductId();
        Long quantity = createReservation.getQuantity();

        Optional<Reservation> existingReservation = reservationRepository.findByOrderIdAndProduct_ProductId(orderId, productId);
        if (existingReservation.isPresent()) {
            throw new ReservationAlreadyExistsException(orderId, productId);
        }

        final Optional<Product> existingProduct = productRepository.findByProductId(productId);
        if (!existingProduct.isPresent()) {
            throw new ProductNotAvailableException(orderId, productId);
        }

        Product product = existingProduct.get();
        if (product.getStock() < quantity) {
            throw new ProductStockInsufficientException(orderId, productId);
        }

        Reservation reservation = Reservation.builder()
                .orderId(orderId)
                .product(product)
                .quantity(quantity)
                .statusReserved()
                .build();
        Reservation savedReservation = reservationRepository.save(reservation);

        LOGGER.info("Created reservation for order-id {} and product-id {}", orderId, productId);

        return convert(savedReservation);
    }

    @Transactional
    public ReservationDto updateReservation(@NotNull final UUID reservationId,
                                            @Valid final UpdateReservationDto updateReservation) {
        Long quantity = updateReservation.getQuantity();
        ReservationStatus status = updateReservation.getStatus();

        Reservation reservation = reservationRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        if (quantity != null) {
            reservation.setQuantity(quantity);
        }
        if (status != null && canUpdateStatus(reservation.getStatus())) {
            reservation.setStatus(status);
        }

        Reservation savedReservation = reservationRepository.save(reservation);

        LOGGER.info("Updated reservation for reservation-id {}", reservationId);

        return convert(savedReservation);
    }

    @Transactional
    public void deleteReservation(@NotNull final UUID reservationId) {
        Reservation reservation = reservationRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        reservation.setStatus(ReservationStatus.CANCELED);
        reservationRepository.save(reservation);

        LOGGER.info("Deleted reservation for reservation-id {}", reservationId);
    }

    private boolean canUpdateStatus(ReservationStatus status) {
        return status != null && status != ReservationStatus.CANCELED && status != ReservationStatus.CONFIRMED;
    }

    private ReservationDto convert(final Reservation reservation) {
        return conversionService.convert(reservation, ReservationDto.class);
    }
}
