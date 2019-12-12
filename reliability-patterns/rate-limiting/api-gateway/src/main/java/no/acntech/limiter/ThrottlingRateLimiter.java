package no.acntech.limiter;

import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cloud.gateway.filter.ratelimit.AbstractRateLimiter;
import org.springframework.cloud.gateway.support.ConfigurationService;

import no.acntech.config.GatewayProperties;
import no.acntech.domain.RateLimitingResponse;
import no.acntech.factory.ThrottlingRequestCounterFactory;

public class ThrottlingRateLimiter extends AbstractRateLimiter<RateLimiterConfig> {

    private static final String CONFIGURATION_PROPERTY_NAME = "rate-limiter";

    private final GatewayProperties gatewayProperties;
    private final ThrottlingRequestCounterFactory throttlingRequestCounterFactory;

    public ThrottlingRateLimiter(final ConfigurationService configurationService,
                                 final GatewayProperties gatewayProperties,
                                 final ThrottlingRequestCounterFactory throttlingRequestCounterFactory) {
        super(RateLimiterConfig.class, CONFIGURATION_PROPERTY_NAME, configurationService);
        this.gatewayProperties = gatewayProperties;
        this.throttlingRequestCounterFactory = throttlingRequestCounterFactory;
    }

    @Override
    public Mono<Response> isAllowed(String routeId, String id) {
        final RateLimiterConfig config = getConfig().getOrDefault(routeId, new RateLimiterConfig());
        return throttlingRequestCounterFactory.create(routeId, id, config.getLimit(), config.getDuration())
                .flatMap(requestCounter -> requestCounter.consume(id))
                .map(this::toResponse);
    }

    private Response toResponse(RateLimitingResponse rateLimitingResponse) {
        if (rateLimitingResponse.isAllowed()) {
            GatewayProperties.RateLimiterProperties throttling = gatewayProperties.getRateLimiter().getThrottling();
            final Map<String, String> headers = new HashMap<>();
            headers.put(throttling.getRemainingRequestsHeader(), String.valueOf(rateLimitingResponse.getRemainingRequests()));
            headers.put(throttling.getRemainingRetryDelayMillisHeader(), String.valueOf(rateLimitingResponse.getRetryDelayMs()));
            return new Response(true, headers);
        } else {
            return new Response(false, Collections.emptyMap());
        }
    }
}
