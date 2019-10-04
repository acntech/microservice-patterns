package no.acntech.reservation.service;

import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import no.acntech.product.model.Product;
import no.acntech.product.repository.ProductRepository;
import no.acntech.reservation.exception.ReservationNotFoundException;
import no.acntech.reservation.model.CancelReservationDto;
import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.Reservation;
import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.ReservationEvent;
import no.acntech.reservation.model.ReservationQuery;
import no.acntech.reservation.model.UpdateReservationDto;
import no.acntech.reservation.producer.ReservationEventProducer;
import no.acntech.reservation.repository.ReservationRepository;

@SuppressWarnings({"Duplicates"})
@Service
public class ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);
    private static final Sort SORT_BY_ID = Sort.by("id");

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
                .map(this::convertDto)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));
    }

    public List<ReservationDto> findReservations(@NotNull final ReservationQuery reservationQuery) {
        UUID orderId = reservationQuery.getOrderId();

        if (orderId == null) {
            return reservationRepository.findAll(SORT_BY_ID)
                    .stream()
                    .map(this::convertDto)
                    .collect(Collectors.toList());
        } else {
            return reservationRepository.findAllByOrderId(orderId, SORT_BY_ID)
                    .stream()
                    .map(this::convertDto)
                    .collect(Collectors.toList());
        }
    }

    public void createReservation(@NotNull final CreateReservationDto createReservation) {
        UUID productId = createReservation.getProductId();
        Long quantity = createReservation.getQuantity();

        Optional<Product> productOptional = productRepository.findByProductId(productId);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            if (product.getStock() >= quantity) {
                createReservationReserved(createReservation, product);
            } else {
                createReservationRejected(createReservation, product);
            }
        } else {
            createReservationFailed(createReservation);
        }
    }

    private void createReservationReserved(@NotNull final CreateReservationDto createReservation,
                                           @NotNull final Product product) {
        UUID orderId = createReservation.getOrderId();
        Long quantity = createReservation.getQuantity();

        Reservation reservation = Reservation.builder()
                .statusReserved()
                .orderId(orderId)
                .product(product)
                .quantity(quantity)
                .build();
        Reservation savedReservation = reservationRepository.save(reservation);

        ReservationEvent event = convertEvent(savedReservation);
        reservationEventProducer.publish(event);
    }

    private void createReservationRejected(@NotNull final CreateReservationDto createReservation,
                                           @NotNull final Product product) {
        UUID orderId = createReservation.getOrderId();
        Long quantity = createReservation.getQuantity();

        Reservation reservation = Reservation.builder()
                .statusRejected()
                .orderId(orderId)
                .product(product)
                .quantity(quantity)
                .build();
        Reservation savedReservation = reservationRepository.save(reservation);

        ReservationEvent event = convertEvent(savedReservation);
        reservationEventProducer.publish(event);
    }

    private void createReservationFailed(@NotNull final CreateReservationDto createReservation) {
        UUID orderId = createReservation.getOrderId();
        Long quantity = createReservation.getQuantity();

        Reservation reservation = Reservation.builder()
                .statusFailed()
                .orderId(orderId)
                .quantity(quantity)
                .build();
        Reservation savedReservation = reservationRepository.save(reservation);

        ReservationEvent event = convertEvent(savedReservation);
        reservationEventProducer.publish(event);
    }

    public void updateAllReservations(@NotNull final UpdateReservationDto updateReservation) {
        UUID orderId = updateReservation.getOrderId();

        List<Reservation> reservations = reservationRepository.findAllByOrderId(orderId, SORT_BY_ID);

        reservations.forEach(reservation -> {
            reservation.confirmReservation();
            Reservation savedReservation = reservationRepository.save(reservation);

            ReservationEvent event = convertEvent(savedReservation);
            reservationEventProducer.publish(event);
        });
    }

    public void updateReservation(@NotNull final UpdateReservationDto updateReservation) {
        UUID orderId = updateReservation.getOrderId();
        UUID productId = updateReservation.getProductId();

        Optional<Reservation> reservationOptional = reservationRepository.findByOrderIdAndProduct_ProductId(orderId, productId);

        if (reservationOptional.isPresent()) {
            Reservation reservation = reservationOptional.get();
            reservation.confirmReservation();
            Reservation savedReservation = reservationRepository.save(reservation);

            ReservationEvent event = convertEvent(savedReservation);
            reservationEventProducer.publish(event);
        } else {
            LOGGER.error("No reservation found for order-id {} and product-id {}", orderId, productId);
        }
    }

    public void cancelAllReservations(@NotNull final CancelReservationDto cancelReservation) {
        UUID orderId = cancelReservation.getOrderId();

        List<Reservation> reservations = reservationRepository.findAllByOrderId(orderId, SORT_BY_ID);

        reservations.forEach(reservation -> {
            reservation.cancelReservation();
            reservationRepository.save(reservation);
        });
    }

    public void cancelReservation(@NotNull final CancelReservationDto cancelReservation) {
        UUID orderId = cancelReservation.getOrderId();
        UUID productId = cancelReservation.getProductId();

        Optional<Reservation> reservationOptional = reservationRepository.findByOrderIdAndProduct_ProductId(orderId, productId);

        if (reservationOptional.isPresent()) {
            Reservation reservation = reservationOptional.get();
            reservation.cancelReservation();
            Reservation savedReservation = reservationRepository.save(reservation);

            ReservationEvent event = convertEvent(savedReservation);
            reservationEventProducer.publish(event);
        } else {
            LOGGER.error("No reservation found for order-id {} and product-id {}", orderId, productId);
        }
    }

    private ReservationDto convertDto(final Reservation reservation) {
        return conversionService.convert(reservation, ReservationDto.class);
    }

    private ReservationEvent convertEvent(final Reservation reservation) {
        return conversionService.convert(reservation, ReservationEvent.class);
    }
}
