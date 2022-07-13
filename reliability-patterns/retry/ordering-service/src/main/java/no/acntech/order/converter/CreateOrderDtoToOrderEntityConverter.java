package no.acntech.order.converter;

import no.acntech.order.model.CreateOrderDto;
import no.acntech.order.model.OrderEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CreateOrderDtoToOrderEntityConverter implements Converter<CreateOrderDto, OrderEntity> {

    @NonNull
    @Override
    public OrderEntity convert(@NonNull final CreateOrderDto source) {
        return OrderEntity.builder()
                .customerId(source.getCustomerId())
                .name(source.getName())
                .description(source.getDescription())
                .build();
    }
}
