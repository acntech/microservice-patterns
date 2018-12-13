package no.acntech.order.integration.shipping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import no.acntech.order.entity.Order;

@Component
public class ShippingRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShippingRestClient.class);

    private final RestTemplate restTemplate;

    @Autowired
    public ShippingRestClient(RestTemplateBuilder restTemplateBuilder) {
        // configure http client (RestTemplate) with timeouts
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(5_000)
                .setReadTimeout(5_000)
                .build();
    }

    /**
     * Calls shipping service and retries on failure.
     * Max retires: 5
     * Waiting before each retry: 2sec
     * Fallback method if max retries is exceeded: {@link this#reoverAfterMaxRetries}
     */
    @Retryable(value = RuntimeException.class, maxAttempts = 5, backoff = @Backoff(2_000))
    public String ship(Order order) {
        ResponseEntity<String> shippingIdResponseEntity = restTemplate.postForEntity("http://localhost:8082/shipments", order.getId(), String.class);
        return shippingIdResponseEntity.getBody();
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    @Recover
    public String reoverAfterMaxRetries(RuntimeException e, Order order) {
        // error handling after exceeding max retries
        LOGGER.warn("Reached max number of retries - OrderId={} Error={}", order.getId(), e.getMessage());
        throw e;
    }

}
