package no.acntech.order.consumer;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

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
