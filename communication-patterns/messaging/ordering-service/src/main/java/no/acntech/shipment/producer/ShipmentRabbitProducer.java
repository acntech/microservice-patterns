package no.acntech.shipment.producer;

import no.acntech.common.config.RabbitQueue;
import no.acntech.shipment.model.CreateShipmentDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Component
public class ShipmentRabbitProducer {

    private final RabbitTemplate rabbitTemplate;

    public ShipmentRabbitProducer(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void create(@NotNull @Valid final CreateShipmentDto createShipmentDto) {
        rabbitTemplate.convertAndSend(RabbitQueue.CREATE_SHIPMENT, createShipmentDto);
    }
}
