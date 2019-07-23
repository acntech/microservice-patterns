package no.acntech.order.consumer;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumerInitializer {

    private final OrderEventConsumer orderEventConsumer;

    public OrderEventConsumerInitializer(final OrderEventConsumer orderEventConsumer) {
        this.orderEventConsumer = orderEventConsumer;
    }

    @PostConstruct
    public void onStartup() {
        orderEventConsumer.startConsumer();
    }
}
