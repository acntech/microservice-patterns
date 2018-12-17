package no.acntech.order.integration.shipping;

import brave.ScopedSpan;
import brave.Tracer;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import no.acntech.order.entity.Order;

@Component
public class ShippingRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShippingRestClient.class);

    private final RestTemplate restTemplate;
    private final Tracer tracer;

    @Autowired
    public ShippingRestClient(RestTemplateBuilder restTemplateBuilder,
                              Tracer tracer) {
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
        this.tracer = tracer;
    }

    public String ship(Order order) {
        ScopedSpan span = tracer.startScopedSpan("ShippingRestClient#ship");
        try {
            LOGGER.debug("Posting to /shipments endpoint for orderId={}", order.getId());
            ResponseEntity<String> shippingIdResponseEntity = restTemplate.postForEntity("http://localhost:8082/shipments", order.getId(), String.class);
            String shippingId = shippingIdResponseEntity.getBody();

            LOGGER.debug("Received response for orderId={}, shipmentId={}" + order.getId(), shippingId);
            return shippingId;
        } catch (RuntimeException | Error e) {
            span.error(e);
            throw e;
        } finally {
            span.finish();
        }
    }
}
