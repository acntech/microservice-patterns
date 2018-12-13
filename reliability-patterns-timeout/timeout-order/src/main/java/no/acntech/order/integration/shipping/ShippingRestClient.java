package no.acntech.order.integration.shipping;

import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import no.acntech.order.entity.Order;

@Component
public class ShippingRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShippingRestClient.class);

    private static final int READ_TIMEOUT_MS = 5_000;
    private static final int CONNECT_TIMEOUT_MS = 5_000;

    private final RestTemplate restTemplate;

    @Autowired
    public ShippingRestClient(RestTemplateBuilder restTemplateBuilder) {
        // configure http client (RestTemplate) with timeouts
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(CONNECT_TIMEOUT_MS)
                .setReadTimeout(READ_TIMEOUT_MS)
                .build();
    }

    /**
     * Call shipping service with specified timeout
     */
    public String ship(Order order) {
        ResponseEntity<String> shippingIdResponseEntity;
        try {
            shippingIdResponseEntity = restTemplate.postForEntity("http://localhost:8082/shipments", order.getId(), String.class);
        } catch (RestClientException e) {
            if (isTimeout(e)) {
                // handle timeout?
                LOGGER.warn("Timeout from shipping service!");
            }

            throw new RuntimeException(e);
        }
        return shippingIdResponseEntity.getBody();
    }

    private boolean isTimeout(RestClientException e) {
        return e.getCause() instanceof SocketTimeoutException;
    }
}
