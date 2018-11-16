package no.acntech.order.integration.shipping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import no.acntech.order.entity.Order;

@Component
public class ShippingRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShippingRestClient.class);

    private final RestTemplate restTemplate;

    @Autowired
    public ShippingRestClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public void ship(Order order) {
        try {
            restTemplate.postForEntity("http://localhost:8082/shipments", order.getWarehouseReservationId(), String.class);
        } catch (Exception e) {
            LOGGER.error("Shipping failed for orderId={}", order.getId());
            throw e;
        }
    }
}
