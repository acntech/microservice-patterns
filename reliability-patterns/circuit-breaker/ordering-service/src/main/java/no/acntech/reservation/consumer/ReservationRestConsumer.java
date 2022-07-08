package no.acntech.reservation.consumer;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.UpdateReservationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("Duplicates")
@Component
public class ReservationRestConsumer {

    private final WebClient webClient;
    private final String url;

    public ReservationRestConsumer(final WebClient webClient,
                                   @Value("${acntech.service.warehouse.url}/api/reservations") final String url) {
        this.webClient = webClient;
        this.url = url;
    }

    @CircuitBreaker(name = "default")
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

    @CircuitBreaker(name = "default")
    public ReservationDto get(@NotNull final UUID reservationId) {
        final var uri = UriComponentsBuilder.fromUriString(url)
                .pathSegment(reservationId.toString())
                .build()
                .toUri();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(ReservationDto.class)
                .block();
    }

    @CircuitBreaker(name = "default")
    public ReservationDto create(@Valid final CreateReservationDto createReservationDto) {
        final var uri = UriComponentsBuilder.fromUriString(url)
                .build()
                .toUri();

        return webClient.post()
                .uri(uri)
                .bodyValue(createReservationDto)
                .retrieve()
                .bodyToMono(ReservationDto.class)
                .block();
    }

    @CircuitBreaker(name = "default")
    public ReservationDto update(@NotNull final UUID reservationId,
                                 @Valid final UpdateReservationDto updateReservationDto) {
        final var uri = UriComponentsBuilder.fromUriString(url)
                .pathSegment(reservationId.toString())
                .build()
                .toUri();

        return webClient.put()
                .uri(uri)
                .bodyValue(updateReservationDto)
                .retrieve()
                .bodyToMono(ReservationDto.class)
                .block();
    }

    @CircuitBreaker(name = "default")
    public ReservationDto delete(@NotNull final UUID reservationId) {
        final var uri = UriComponentsBuilder.fromUriString(url)
                .pathSegment(reservationId.toString())
                .build()
                .toUri();

        return webClient.delete()
                .uri(uri)
                .retrieve()
                .bodyToMono(ReservationDto.class)
                .block();
    }
}
