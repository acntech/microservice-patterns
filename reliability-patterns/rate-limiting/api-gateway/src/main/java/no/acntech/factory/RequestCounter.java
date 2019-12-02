package no.acntech.factory;

import no.acntech.domain.ConsumeResponse;
import reactor.core.publisher.Mono;

public interface RequestCounter {

    Mono<ConsumeResponse> consume(String apiKey);
}
