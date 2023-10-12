package no.acntech.common.config;

import no.acntech.reservation.model.ReservationEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

@SuppressWarnings("Duplicates")
@EnableKafka
@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic reservationsTopic() {
        return new NewTopic(KafkaTopic.RESERVATIONS, 1, (short) 1);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(final ProducerFactory<String, Object> kafkaProducerFactory) {
        return new KafkaTemplate<>(kafkaProducerFactory);
    }

    @Bean
    public ProducerFactory<String, Object> kafkaProducerFactory(final KafkaProperties kafkaProperties) {
        return new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties());
    }

    @Bean
    public ConsumerFactory<String, ReservationEvent> reservationKafkaConsumerFactory(final KafkaProperties kafkaProperties) {
        return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, ReservationEvent>> reservationKafkaListenerContainerFactory(
            final ConsumerFactory<String, ReservationEvent> reservationKafkaConsumerFactory) {
        final var factory = new ConcurrentKafkaListenerContainerFactory<String, ReservationEvent>();
        factory.setConsumerFactory(reservationKafkaConsumerFactory);
        return factory;
    }
}
