package no.acntech.factory;

import no.acntech.domain.RateLimitingResponse;
import reactor.core.publisher.Mono;

public interface RequestCounter {

    Mono<RateLimitingResponse> consume(String apiKey);
}
