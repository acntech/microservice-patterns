package no.acntech.limiter;

import no.acntech.domain.ConsumeResponse;
import no.acntech.factory.RequestCounterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.AbstractRateLimiter;
import org.springframework.cloud.gateway.support.ConfigurationService;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultRateLimiter extends AbstractRateLimiter<RateLimiterConfig> {

    private static final String CONFIGURATION_PROPERTY_NAME = "rate-limiter";

    private final RequestCounterFactory requestCounterFactory;

    public DefaultRateLimiter(final ConfigurationService configurationService,
                              final RequestCounterFactory requestCounterFactory) {
        super(RateLimiterConfig.class, CONFIGURATION_PROPERTY_NAME, configurationService);
        this.requestCounterFactory = requestCounterFactory;
    }

    @Override
    public Mono<Response> isAllowed(String routeId, String id) {
        final RateLimiterConfig config = getConfig().getOrDefault(routeId, new RateLimiterConfig());
        return requestCounterFactory.create(routeId, id, config.getLimit(), config.getDuration())
                .flatMap(requestCounter -> requestCounter.consume(id))
                .map(this::toResponse);
    }

    private Response toResponse(ConsumeResponse consumeResponse) {
        if (consumeResponse.isAllowed()) {
            final Map<String, String> headers = new HashMap<>();
            headers.put("X-Remaining", String.valueOf(consumeResponse.getRemainingRequests()));
            headers.put("X-Retry-In", String.valueOf(consumeResponse.getRetryDelayMs()));
            return new Response(true, headers);
        } else {
            return new Response(false, Collections.emptyMap());
        }
    }
}
