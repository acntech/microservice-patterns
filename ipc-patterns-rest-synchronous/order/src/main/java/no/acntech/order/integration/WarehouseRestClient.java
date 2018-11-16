package no.acntech.order.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import no.acntech.order.entity.Order;

@Component
public class WarehouseRestClient {

    private final RestTemplate restTemplate;

    @Autowired
    public WarehouseRestClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public void reserve(Order order) {
        restTemplate.postForEntity("http://localhost:8080/reservations", InventoryReservation.createFromOrder(order), ResponseEntity.class);
    }
}
