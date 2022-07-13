package no.acntech.limiter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import no.acntech.config.RateLimiterProperties;
import org.springframework.cloud.gateway.filter.ratelimit.AbstractRateLimiter;
import org.springframework.cloud.gateway.support.ConfigurationService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;

@Component
public class DefaultRateLimiter extends AbstractRateLimiter<RateLimiterProperties> {

    private static final String CONFIGURATION_PROPERTY_NAME = "rate-limiter";
    private static final String REMAINING_REQUESTS_HEADER = "X-Rate-Limit-Remaining-Requests";
    private static final String RETRY_DELAY_MILLIS = "X-Rate-Limit-Retry-Delay-Millis";
    private final ProxyManager<String> proxyManager;

    public DefaultRateLimiter(final ConfigurationService configurationService,
                              final ProxyManager<String> proxyManager) {
        super(RateLimiterProperties.class, CONFIGURATION_PROPERTY_NAME, configurationService);
        this.proxyManager = proxyManager;
    }

    @Override
    public Mono<Response> isAllowed(String routeId, String key) {
        final var bucket = toBucket(routeId, key);
        final var consumptionProbe = bucket.tryConsumeAndReturnRemaining(1);
        return Mono.just(toResponse(consumptionProbe));
    }

    private Bucket toBucket(String routeId, String key) {
        final var bucketConfig = getConfig().getOrDefault(routeId, new RateLimiterProperties());
        final var bucketConfiguration = proxyManager.getProxyConfiguration(key)
                .orElseGet(() -> createBucketConfiguration(bucketConfig));
        return proxyManager.builder()
                .build(key, bucketConfiguration);
    }

    private Response toResponse(final ConsumptionProbe rateLimit) {
        if (rateLimit.isConsumed()) {
            final var headers = new HashMap<String, String>();
            headers.put(REMAINING_REQUESTS_HEADER, String.valueOf(rateLimit.getRemainingTokens()));
            headers.put(RETRY_DELAY_MILLIS, String.valueOf(rateLimit.getNanosToWaitForReset() / 1000));
            return new Response(true, headers);
        } else {
            return new Response(false, Collections.emptyMap());
        }
    }

    private BucketConfiguration createBucketConfiguration(final RateLimiterProperties rateLimiterProperties) {
        final var bandwidth = Bandwidth
                .simple(rateLimiterProperties.getLimit(), rateLimiterProperties.getDuration());
        return BucketConfiguration.builder()
                .addLimit(bandwidth)
                .build();
    }
}
