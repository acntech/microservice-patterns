package no.acntech.reservation.consumer;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.UpdateReservationDto;

@SuppressWarnings("Duplicates")
@Component
public class ReservationRestConsumer {

    private static final ParameterizedTypeReference<List<ReservationDto>> RESERVATION_LIST = new ParameterizedTypeReference<List<ReservationDto>>() {
    };
    private final RestTemplate restTemplate;
    private final String url;
    private final String idUrl;

    public ReservationRestConsumer(final RestTemplateBuilder restTemplateBuilder,
                                   @Value("${acntech.service.warehouse.api.reservations.url}") final String url,
                                   @Value("${acntech.service.warehouse.api.reservations.id.url}") final String idUrl) {
        this.restTemplate = restTemplateBuilder.build();
        this.url = url;
        this.idUrl = idUrl;
    }

    public List<ReservationDto> find() {
        final URI uri = UriComponentsBuilder.fromUriString(url)
                .build()
                .toUri();

        final ResponseEntity<List<ReservationDto>> entity = restTemplate.exchange(uri, HttpMethod.GET, null, RESERVATION_LIST);

        return entity.getBody();
    }

    public Optional<ReservationDto> get(@NotNull final UUID reservationId) {
        Assert.notNull(reservationId, "Reservation ID is null");

        final URI uri = UriComponentsBuilder.fromUriString(idUrl)
                .buildAndExpand(reservationId.toString())
                .toUri();

        final ResponseEntity<ReservationDto> entity = restTemplate.getForEntity(uri, ReservationDto.class);
        return Optional.of(entity)
                .map(ResponseEntity::getBody);
    }

    public void create(@Valid final CreateReservationDto createReservation) {
        final URI uri = UriComponentsBuilder.fromUriString(url)
                .build()
                .toUri();

        restTemplate.postForEntity(uri, createReservation, Void.class);
    }

    public void update(@Valid final UpdateReservationDto updateReservation) {
        final URI uri = UriComponentsBuilder.fromUriString(url)
                .build()
                .toUri();

        restTemplate.put(uri, updateReservation);
    }
}
