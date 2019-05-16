package no.acntech.order.producer;

import javax.validation.constraints.NotNull;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import no.acntech.common.config.KafkaTopic;
import no.acntech.order.model.OrderEvent;

@Transactional
@Component
public class OrderEventProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderEventProducer(final KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(@NotNull final UUID orderId) {
        LOGGER.debug("Sending event for order-id {} to topic {}", orderId, KafkaTopic.ORDERS.getName());
        OrderEvent orderEvent = OrderEvent.builder()
                .orderId(orderId)
                .build();
        kafkaTemplate.send(KafkaTopic.ORDERS.getName(), orderEvent);
    }
}
