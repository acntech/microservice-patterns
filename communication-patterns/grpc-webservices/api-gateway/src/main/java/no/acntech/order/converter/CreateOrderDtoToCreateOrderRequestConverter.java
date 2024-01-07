package no.acntech.order.converter;

import no.acntech.order.model.CreateOrderDto;
import no.acntech.order.model.CreateOrderRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CreateOrderDtoToCreateOrderRequestConverter implements Converter<CreateOrderDto, CreateOrderRequest> {

    @NonNull
    @Override
    public CreateOrderRequest convert(@NonNull final CreateOrderDto source) {
        return CreateOrderRequest.newBuilder()
                .setBody(source)
                .build();
    }
}
