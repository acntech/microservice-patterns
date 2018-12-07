package no.acntech.order.producer;

import no.acntech.order.model.OrderEvent;
import no.acntech.order.model.OrderEventType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class OrderEventProducer {

    private static final String KAFKA_TOPIC = "orders";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderEventProducer(final KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public void orderCreated(UUID orderId) {
        kafkaTemplate.send(KAFKA_TOPIC, OrderEvent.builder()
                .type(OrderEventType.ORDER_CREATED)
                .orderId(orderId)
                .build());
    }

    @Transactional
    public void orderUpdated(UUID orderId, UUID productId, Long quantity) {
        kafkaTemplate.send(KAFKA_TOPIC, OrderEvent.builder()
                .type(OrderEventType.ORDER_UPDATED)
                .orderId(orderId)
                .productId(productId)
                .quantity(quantity)
                .build());
    }

    @Transactional
    public void orderCompleted(UUID orderId) {
        kafkaTemplate.send(KAFKA_TOPIC, OrderEvent.builder()
                .type(OrderEventType.ORDER_COMPLETED)
                .orderId(orderId)
                .build());
    }
}
