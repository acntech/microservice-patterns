package no.acntech.order.converter;

import no.acntech.order.model.CreateOrder;
import no.acntech.order.model.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Component
public class CreateOrderConverter implements Converter<CreateOrder, Order> {

    @Override
    public Order convert(@Valid @NotNull final CreateOrder createOrder) {
        return Order.builder()
                .customerId(createOrder.getCustomerId())
                .build();
    }
}
