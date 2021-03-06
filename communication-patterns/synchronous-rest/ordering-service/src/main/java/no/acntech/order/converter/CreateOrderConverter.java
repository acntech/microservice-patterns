package no.acntech.order.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import no.acntech.order.model.CreateOrderDto;
import no.acntech.order.model.Order;

@Component
public class CreateOrderConverter implements Converter<CreateOrderDto, Order> {

    @Override
    public Order convert(@NonNull final CreateOrderDto createOrder) {
        return Order.builder()
                .customerId(createOrder.getCustomerId())
                .name(createOrder.getName())
                .description(createOrder.getDescription())
                .build();
    }
}
