package no.acntech.factory;

import io.github.bucket4j.AsyncBucket;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import no.acntech.domain.ConsumeResponse;
import reactor.core.publisher.Mono;

public class DefaultRequestCounter implements RequestCounter {

    private final AsyncBucket bucket;

    public DefaultRequestCounter(final Bucket bucket) {
        this.bucket = bucket.asAsync();
    }

    @Override
    public Mono<ConsumeResponse> consume(String apiKey) {
        return Mono.fromFuture(bucket.tryConsumeAndReturnRemaining(1))
                .map(this::createConsumeResponse);
    }

    private ConsumeResponse createConsumeResponse(ConsumptionProbe consumptionProbe) {
        ConsumeResponse consumeResponse = new ConsumeResponse();
        consumeResponse.setAllowed(consumptionProbe.isConsumed());
        consumeResponse.setRemainingRequests(consumptionProbe.getRemainingTokens());
        consumeResponse.setRetryDelayMs(consumptionProbe.getNanosToWaitForRefill() * 1000);
        return consumeResponse;
    }
}
