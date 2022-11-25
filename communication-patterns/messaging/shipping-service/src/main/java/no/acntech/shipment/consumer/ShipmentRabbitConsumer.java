package no.acntech.shipment.consumer;

import no.acntech.common.config.RabbitQueue;
import no.acntech.shipment.model.CreateShipmentDto;
import no.acntech.shipment.service.ShipmentService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Validated
@Component
public class ShipmentRabbitConsumer {

    private final ShipmentService shipmentService;

    public ShipmentRabbitConsumer(final ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @RabbitListener(queues = RabbitQueue.CREATE_SHIPMENT)
    public void consumeCreateShipment(@NotNull @Valid final CreateShipmentDto createShipmentDto) {
        shipmentService.createShipment(createShipmentDto);
    }
}
