package no.acntech.converter;

import no.acntech.order.model.CreateOrderLine;
import no.acntech.order.model.OrderLine;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Component
public class CreateOrderLineConverter implements Converter<CreateOrderLine, OrderLine> {

    @Override
    public OrderLine convert(@Valid @NotNull final CreateOrderLine createOrderLine) {
        return OrderLine.builder()
                .orderId(createOrderLine.getOrderId())
                .productId(createOrderLine.getProductId())
                .quantity(createOrderLine.getQuantity())
                .build();
    }
}
