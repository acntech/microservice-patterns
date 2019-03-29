package no.acntech.order.consumer;

import javax.validation.constraints.NotNull;

import java.net.URI;
import java.util.List;
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

import no.acntech.reservation.model.ReservationDto;

@SuppressWarnings("Duplicates")
@Component
public class OrderRestConsumer {

    private final RestTemplate restTemplate;
    private final String url;
    private final String singleUrl;

    public OrderRestConsumer(final RestTemplateBuilder restTemplateBuilder,
                             @Value("${acntech.service.ordering.api.orders.url}") final String url,
                             @Value("${acntech.service.ordering.api.orders.url}") final String singleUrl) {
        this.restTemplate = restTemplateBuilder.build();
        this.url = url;
        this.singleUrl = singleUrl;
    }

    public List<ReservationDto> find() {
        final URI uri = UriComponentsBuilder.fromUriString(url)
                .build()
                .toUri();

        ParameterizedTypeReference<List<ReservationDto>> typeReference = new ParameterizedTypeReference<List<ReservationDto>>() {
        };
        final ResponseEntity<List<ReservationDto>> entity = restTemplate.exchange(uri, HttpMethod.GET, null, typeReference);

        return entity.getBody();
    }

    public ReservationDto get(@NotNull final UUID reservationId) {
        Assert.notNull(reservationId, "Reservation ID is null");

        final URI uri = UriComponentsBuilder.fromUriString(singleUrl)
                .pathSegment(reservationId.toString())
                .build()
                .toUri();

        final ResponseEntity<ReservationDto> entity = restTemplate.getForEntity(uri, ReservationDto.class);
        return entity.getBody();
    }
}
