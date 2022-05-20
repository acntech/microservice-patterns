package no.acntech.order.consumer;

import no.acntech.common.config.KafkaTopic;
import no.acntech.order.model.OrderEvent;
import no.acntech.order.service.OrderService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@SuppressWarnings("Duplicates")
@Component
public class OrderEventConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventConsumer.class);
    private final OrderService orderService;

    public OrderEventConsumer(final OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = KafkaTopic.ORDERS)
    public void consume(final ConsumerRecord<String, OrderEvent> record) {
        final var orderEvent = record.value();
        LOGGER.debug("Received order event with order-id {} from topic {}", orderEvent.getOrderId(), KafkaTopic.ORDERS);
        orderService.receiveOrderEvent(orderEvent);
    }
}
