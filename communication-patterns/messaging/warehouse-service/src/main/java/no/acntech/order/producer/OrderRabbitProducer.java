package no.acntech.order.producer;

import no.acntech.common.config.RabbitQueue;
import no.acntech.order.model.UpdateOrderItemReservationDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Validated
@Component
public class OrderRabbitProducer {

    private final RabbitTemplate rabbitTemplate;

    public OrderRabbitProducer(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void updateOrderItem(@NotNull @Valid final UpdateOrderItemReservationDto updateOrderItemReservationDto) {
        rabbitTemplate.convertAndSend(RabbitQueue.UPDATE_ORDER_ITEM, updateOrderItemReservationDto);
    }
}
