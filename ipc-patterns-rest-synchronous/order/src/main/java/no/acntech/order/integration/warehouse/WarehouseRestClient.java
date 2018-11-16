package no.acntech.order.integration.warehouse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import no.acntech.order.entity.Order;

@Component
public class WarehouseRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseRestClient.class);

    private final RestTemplate restTemplate;

    @Autowired
    public WarehouseRestClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String reserve(Order order) {
        try {
            ResponseEntity<String> reservationId = restTemplate.postForEntity("http://localhost:8081/reservations", InventoryReservation.createFromOrder(order), String.class);
            return reservationId.getBody();
        } catch (Exception e) {
            LOGGER.error("Warehouse reservation failed for orderId={}", order.getId());
            throw e;
        }
    }
}
