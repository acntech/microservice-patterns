package no.acntech.reservation.consumer;

import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.PendingReservationDto;
import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.UpdateReservationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("Duplicates")
@Component
public class ReservationRestConsumer {

    private final WebClient webClient;
    private final String url;

    public ReservationRestConsumer(final WebClient webClient,
                                   @Value("${app.service.warehouse.url}/api/reservations") final String url) {
        this.webClient = webClient;
        this.url = url;
    }

    public List<ReservationDto> find() {
        final var uri = UriComponentsBuilder.fromUriString(url)
                .build()
                .toUri();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(ReservationDto.class)
                .collectList()
                .block();
    }

    public Optional<ReservationDto> get(@NotNull final UUID reservationId) {
        final var uri = UriComponentsBuilder.fromUriString(url)
                .pathSegment(reservationId.toString())
                .build()
                .toUri();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(ReservationDto.class)
                .blockOptional();
    }

    public Optional<PendingReservationDto> create(@Valid final CreateReservationDto createReservationDto) {
        final var uri = UriComponentsBuilder.fromUriString(url)
                .build()
                .toUri();

        return webClient.post()
                .uri(uri)
                .bodyValue(createReservationDto)
                .retrieve()
                .bodyToMono(PendingReservationDto.class)
                .blockOptional();
    }

    public void update(@NotNull final UUID reservationId,
                       @Valid final UpdateReservationDto updateReservationDto) {
        final var uri = UriComponentsBuilder.fromUriString(url)
                .pathSegment(reservationId.toString())
                .build()
                .toUri();

        webClient.put()
                .uri(uri)
                .bodyValue(updateReservationDto)
                .retrieve();
    }

    public void delete(@NotNull final UUID reservationId) {
        final var uri = UriComponentsBuilder.fromUriString(url)
                .pathSegment(reservationId.toString())
                .build()
                .toUri();

        webClient.delete()
                .uri(uri)
                .retrieve();
    }
}
