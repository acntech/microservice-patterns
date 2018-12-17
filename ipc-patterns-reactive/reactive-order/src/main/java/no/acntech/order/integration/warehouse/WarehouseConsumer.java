package no.acntech.order.integration.warehouse;

import reactor.core.publisher.Mono;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import no.acntech.order.entity.Order;

@Component
public class WarehouseConsumer {

    public Mono<Void> reserve(Order order) {
        WebClient webClient = WebClient.create("http://localhost:8081/reservations");
      return   webClient.post()
                .body(BodyInserters.fromObject(InventoryReservation.createFromOrder(order)))
                .exchange()
                .flatMap(response -> response.bodyToMono(Void.class));
//        return webClient
//                .method(HttpMethod.POST)
//                .uri("/reservations")
//                .body(BodyInserters.fromObject(InventoryReservation.createFromOrder(order)))
//                .retrieve()
//                .bodyToMono(Void.class);

    }
}
