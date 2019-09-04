package no.acntech.reservation.service;

import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import no.acntech.product.model.Product;
import no.acntech.product.repository.ProductRepository;
import no.acntech.reservation.exception.ReservationNotFoundException;
import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.Reservation;
import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.ReservationEvent;
import no.acntech.reservation.model.ReservationEventType;
import no.acntech.reservation.model.ReservationQuery;
import no.acntech.reservation.producer.ReservationEventProducer;
import no.acntech.reservation.repository.ReservationRepository;

@SuppressWarnings({"Duplicates"})
@Service
public class ReservationService {

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
                .map(this::convert)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));
    }

    public List<ReservationDto> findReservations(@NotNull final ReservationQuery reservationQuery) {
        UUID orderId = reservationQuery.getOrderId();

        if (orderId == null) {
            return reservationRepository.findAll(SORT_BY_ID)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else {
            return reservationRepository.findAllByOrderId(orderId, SORT_BY_ID)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        }
    }

    public void createReservation(@NotNull final CreateReservationDto createReservation) {
        UUID orderId = createReservation.getOrderId();
        UUID productId = createReservation.getProductId();
        Long quantity = createReservation.getQuantity();

        Optional<Product> productOptional = productRepository.findByProductId(productId);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            if (product.getStock() >= quantity) {
                Reservation reservation = Reservation.builder()
                        .reservationId(UUID.randomUUID())
                        .statusReserved()
                        .orderId(orderId)
                        .product(product)
                        .quantity(quantity)
                        .build();
                Reservation savedReservation = reservationRepository.save(reservation);

                ReservationEvent event = ReservationEvent.builder()
                        .eventType(ReservationEventType.RESERVATION_CREATED)
                        .reservationId(savedReservation.getReservationId())
                        .status(savedReservation.getStatus())
                        .orderId(orderId)
                        .productId(productId)
                        .quantity(quantity)
                        .build();
                reservationEventProducer.publish(event);
            } else {
                Reservation reservation = Reservation.builder()
                        .reservationId(UUID.randomUUID())
                        .statusRejected()
                        .orderId(orderId)
                        .product(product)
                        .quantity(quantity)
                        .build();
                Reservation savedReservation = reservationRepository.save(reservation);

                ReservationEvent event = ReservationEvent.builder()
                        .eventType(ReservationEventType.RESERVATION_REJECTED)
                        .reservationId(savedReservation.getReservationId())
                        .status(savedReservation.getStatus())
                        .orderId(orderId)
                        .productId(productId)
                        .quantity(quantity)
                        .build();
                reservationEventProducer.publish(event);
            }
        } else {
            Reservation reservation = Reservation.builder()
                    .reservationId(UUID.randomUUID())
                    .statusFailed()
                    .orderId(orderId)
                    .quantity(quantity)
                    .build();
            Reservation savedReservation = reservationRepository.save(reservation);

            ReservationEvent event = ReservationEvent.builder()
                    .eventType(ReservationEventType.RESERVATION_FAILED)
                    .reservationId(savedReservation.getReservationId())
                    .status(savedReservation.getStatus())
                    .orderId(orderId)
                    .productId(productId)
                    .quantity(quantity)
                    .build();
            reservationEventProducer.publish(event);
        }
    }

    private ReservationDto convert(final Reservation reservation) {
        return conversionService.convert(reservation, ReservationDto.class);
    }
}
