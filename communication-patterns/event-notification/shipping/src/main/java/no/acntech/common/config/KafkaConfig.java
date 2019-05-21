package no.acntech.common.config;

import java.util.Arrays;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import no.acntech.order.model.OrderEvent;

@SuppressWarnings("Duplicates")
@EnableKafka
@Configuration
public class KafkaConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConfig.class);
    private final KafkaProperties kafkaProperties;

    public KafkaConfig(final KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
        LOGGER.info("Configuring Kafka, connecting to bootstrap servers {}...", Arrays.toString(kafkaProperties.getBootstrapServers().toArray()));
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(kafkaProducerFactory());
    }

    @Bean
    public ProducerFactory<String, Object> kafkaProducerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties());
    }

    @Bean
    public KafkaConsumer<String, OrderEvent> orderKafkaConsumer() {
        return new KafkaConsumer<>(kafkaProperties.buildConsumerProperties());
    }
}
