package no.acntech.factory;

import io.github.bucket4j.AsyncBucket;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import no.acntech.domain.RateLimitingResponse;
import reactor.core.publisher.Mono;

public class ThrottlingRequestCounter implements RequestCounter {

    private final AsyncBucket bucket;

    ThrottlingRequestCounter(final Bucket bucket) {
        this.bucket = bucket.asAsync();
    }

    @Override
    public Mono<RateLimitingResponse> consume(String apiKey) {
        return Mono.fromFuture(bucket.tryConsumeAndReturnRemaining(1))
                .map(this::createConsumeResponse);
    }

    private RateLimitingResponse createConsumeResponse(ConsumptionProbe consumptionProbe) {
        RateLimitingResponse rateLimitingResponse = new RateLimitingResponse();
        rateLimitingResponse.setAllowed(consumptionProbe.isConsumed());
        rateLimitingResponse.setRemainingRequests(consumptionProbe.getRemainingTokens());
        rateLimitingResponse.setRetryDelayMs(consumptionProbe.getNanosToWaitForRefill() * 1000);
        return rateLimitingResponse;
    }
}
