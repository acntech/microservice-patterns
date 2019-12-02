package no.acntech.factory;

import reactor.core.publisher.Mono;

import java.time.Duration;

public interface RequestCounterFactory {

    Mono<RequestCounter> create(String routeId, String apiKey, int limit, Duration duration);
}
