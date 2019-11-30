package no.acntech.reservation.consumer;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.UpdateReservationDto;

@SuppressWarnings("Duplicates")
@Component
public class ReservationRestConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationRestConsumer.class);
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

    /**
     * Example of hystrix command
     * <p>
     * List of hystrix command properties: https://github.com/Netflix/Hystrix/wiki/Configuration#CommandExecution
     * <p>
     * The following configuration specifies:
     * - Timeout and error if call takes more than 2 seconds.
     * - If we in 10 seconds (metrics.rollingStats.timeInMilliseconds) receive at least 5 requests (circuitBreaker.requestVolumeThreshold)
     * - Check if 50% or more has failed (circuitBreaker.errorThresholdPercentage)
     * -- If < 50%, let the circuit stay closed (call downstream)
     * -- If > 50%, open the circuit for 10 seconds (circuitBreaker.sleepWindowInMilliseconds). After that, let the first request be served on closed circuit (call downstream)
     * ---- If the request is OK, close the circuit and start measuring again
     * ---- If first request fails, keep the circuit open for 10 more seconds and retry
     */
    @HystrixCommand(fallbackMethod = "createOrderFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"), // timeout
            @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000"), // time window to collect information
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"), // number of requests in time window required to measure
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"), // if requestVolumeThreshold is reached, this is the error percentage before tripping the circuit
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), // time to wait after tripping the circuit
    })
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

    /**
     * Fallback for the {@link this#create} hystrix command.
     * Simply returns -1 in this simple example. Fallback logic needs functional mapping (retry reservation, etc?)
     */
    @SuppressWarnings("unused")
    public String createOrderFallback(final CreateReservationDto createReservation) {
        LOGGER.debug("Create reservation failed for orderId=" + createReservation.getOrderId());
        return "-1";
    }
}
