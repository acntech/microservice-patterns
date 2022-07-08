package no.acntech.reservation.consumer;

import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.UpdateReservationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("Duplicates")
@Component
public class ReservationRestConsumer {

    private static final ParameterizedTypeReference<List<ReservationDto>> RESERVATION_LIST = new ParameterizedTypeReference<List<ReservationDto>>() {
    };
    private final RestTemplate restTemplate;
    private final String url;

    public ReservationRestConsumer(final RestTemplateBuilder restTemplateBuilder,
                                   @Value("${acntech.service.warehouse.api.reservations.url}") final String url) {
        this.restTemplate = restTemplateBuilder.build();
        this.url = url;
    }

    public List<ReservationDto> find() {
        final URI uri = UriComponentsBuilder.fromUriString(url)
                .build()
                .toUri();

        final ResponseEntity<List<ReservationDto>> entity = restTemplate.exchange(uri, HttpMethod.GET, null, RESERVATION_LIST);

        return entity.getBody();
    }

    public Optional<ReservationDto> get(@NotNull final UUID reservationId) {
        final URI uri = UriComponentsBuilder.fromUriString(url)
                .pathSegment(reservationId.toString())
                .build()
                .toUri();

        final ResponseEntity<ReservationDto> entity = restTemplate.getForEntity(uri, ReservationDto.class);
        return Optional.of(entity)
                .map(ResponseEntity::getBody);
    }

    public Optional<ReservationDto> create(@Valid final CreateReservationDto createReservation) {
        final URI uri = UriComponentsBuilder.fromUriString(url)
                .build()
                .toUri();

        ResponseEntity<ReservationDto> entity = restTemplate.postForEntity(uri, createReservation, ReservationDto.class);
        return Optional.of(entity)
                .map(ResponseEntity::getBody);
    }

    public Optional<ReservationDto> update(@NotNull final UUID reservationId,
                                           @Valid final UpdateReservationDto updateReservation) {
        final URI uri = UriComponentsBuilder.fromUriString(url)
                .pathSegment(reservationId.toString())
                .build()
                .toUri();

        final HttpEntity<UpdateReservationDto> httpEntity = new HttpEntity<>(updateReservation);
        final ResponseEntity<ReservationDto> entity = restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, ReservationDto.class);
        return Optional.of(entity)
                .map(ResponseEntity::getBody);
    }

    public Optional<ReservationDto> delete(@NotNull final UUID reservationId) {
        final URI uri = UriComponentsBuilder.fromUriString(url)
                .pathSegment(reservationId.toString())
                .build()
                .toUri();

        final ResponseEntity<ReservationDto> entity = restTemplate.exchange(uri, HttpMethod.DELETE, new HttpEntity<>(new HttpHeaders()), ReservationDto.class);
        return Optional.of(entity)
                .map(ResponseEntity::getBody);
    }
}
