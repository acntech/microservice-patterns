package no.acntech.testdata;

import javax.annotation.PostConstruct;

import java.util.UUID;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import no.acntech.order.model.Order;
import no.acntech.order.repository.OrderRepository;

@Component
public class TestData {

    private final OrderRepository orderRepository;

    public TestData(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostConstruct
    public void insertTestData() {
        IntStream.range(0, 10)
                .boxed()
                .map(name -> Order.builder()
                        .customerId(UUID.randomUUID())
                        .build())
                .forEach(orderRepository::save);
    }
}
