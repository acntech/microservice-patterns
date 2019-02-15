package no.acntech.reservation.config;

import no.acntech.reservation.model.ReservationEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("Duplicates")
@Configuration
public class ReservationKafkaConfig {

    @Bean
    public KafkaConsumer<String, ReservationEvent> reservationKafkaConsumer() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(ConsumerConfig.CLIENT_ID_CONFIG, "event-notification-ordering");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "event-notification-ordering");
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new KafkaConsumer<>(config);
    }
}
