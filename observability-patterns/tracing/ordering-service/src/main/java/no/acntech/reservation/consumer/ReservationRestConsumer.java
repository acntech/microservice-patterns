package no.acntech.reservation.consumer;

import brave.ScopedSpan;
import brave.Tracer;
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
    private final Tracer tracer;
    private final RestTemplate restTemplate;
    private final String url;

    public ReservationRestConsumer(final Tracer tracer,
                                   final RestTemplateBuilder restTemplateBuilder,
                                   @Value("${acntech.service.warehouse.api.reservations.url}") final String url) {
        this.tracer = tracer;
        this.restTemplate = restTemplateBuilder.build();
        this.url = url;
    }

    public List<ReservationDto> find() {
        ScopedSpan span = tracer.startScopedSpan("ReservationRestConsumer#find");
        try {
            final URI uri = UriComponentsBuilder.fromUriString(url)
                    .build()
                    .toUri();

            final ResponseEntity<List<ReservationDto>> entity = restTemplate.exchange(uri, HttpMethod.GET, null, RESERVATION_LIST);

            return entity.getBody();
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    public Optional<ReservationDto> get(@NotNull final UUID reservationId) {
        ScopedSpan span = tracer.startScopedSpan("ReservationRestConsumer#get");
        try {
            final URI uri = UriComponentsBuilder.fromUriString(url)
                    .pathSegment(reservationId.toString())
                    .build()
                    .toUri();

            final ResponseEntity<ReservationDto> entity = restTemplate.getForEntity(uri, ReservationDto.class);
            return Optional.of(entity)
                    .map(ResponseEntity::getBody);
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    public Optional<ReservationDto> create(@Valid final CreateReservationDto createReservation) {
        ScopedSpan span = tracer.startScopedSpan("ReservationRestConsumer#create");
        try {
            final URI uri = UriComponentsBuilder.fromUriString(url)
                    .build()
                    .toUri();

            ResponseEntity<ReservationDto> entity = restTemplate.postForEntity(uri, createReservation, ReservationDto.class);
            return Optional.of(entity)
                    .map(ResponseEntity::getBody);
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    public Optional<ReservationDto> update(@NotNull final UUID reservationId,
                                           @Valid final UpdateReservationDto updateReservation) {
        ScopedSpan span = tracer.startScopedSpan("ReservationRestConsumer#update");
        try {
            final URI uri = UriComponentsBuilder.fromUriString(url)
                    .pathSegment(reservationId.toString())
                    .build()
                    .toUri();

            final HttpEntity<UpdateReservationDto> httpEntity = new HttpEntity<>(updateReservation);
            final ResponseEntity<ReservationDto> entity = restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, ReservationDto.class);
            return Optional.of(entity)
                    .map(ResponseEntity::getBody);
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    public Optional<ReservationDto> delete(@NotNull final UUID reservationId) {
        ScopedSpan span = tracer.startScopedSpan("ReservationRestConsumer#delete");
        try {
            final URI uri = UriComponentsBuilder.fromUriString(url)
                    .pathSegment(reservationId.toString())
                    .build()
                    .toUri();

            final ResponseEntity<ReservationDto> entity = restTemplate.exchange(uri, HttpMethod.DELETE, new HttpEntity<>(new HttpHeaders()), ReservationDto.class);
            return Optional.of(entity)
                    .map(ResponseEntity::getBody);
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }
}
