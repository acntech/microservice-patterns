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
import no.acntech.reservation.model.ReservationQuery;
import no.acntech.reservation.repository.ReservationRepository;

@SuppressWarnings({"Duplicates"})
@Service
public class ReservationService {

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
        UUID productId = createReservation.getProductId();
        UUID orderId = createReservation.getOrderId();
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
                reservationRepository.save(reservation);
            } else {
                Reservation reservation = Reservation.builder()
                        .reservationId(UUID.randomUUID())
                        .statusRejected()
                        .orderId(orderId)
                        .product(product)
                        .quantity(quantity)
                        .build();
                reservationRepository.save(reservation);
            }
        } else {
            Reservation reservation = Reservation.builder()
                    .reservationId(UUID.randomUUID())
                    .statusFailed()
                    .orderId(orderId)
                    .quantity(quantity)
                    .build();
            reservationRepository.save(reservation);
        }
    }

    private ReservationDto convert(final Reservation reservation) {
        return conversionService.convert(reservation, ReservationDto.class);
    }
}
