package no.acntech.order.consumer;

import no.acntech.common.config.RabbitQueue;
import no.acntech.order.model.UpdateOrderItemReservationDto;
import no.acntech.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@Component
public class OrderRabbitConsumer {

    private final OrderService orderService;

    public OrderRabbitConsumer(final OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = RabbitQueue.UPDATE_ORDER_ITEM)
    public void consumeUpdateOrderItem(@NotNull @Valid final UpdateOrderItemReservationDto updateOrderItemReservationDto) {
        orderService.updateOrderItemReservation(updateOrderItemReservationDto);
    }
}
