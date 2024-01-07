package no.acntech.order.converter;

import no.acntech.order.model.GetOrderRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GetOrderRequestToUUIDConverter implements Converter<GetOrderRequest, UUID> {

    @Nullable
    @Override
    public UUID convert(@NonNull final GetOrderRequest source) {
        if (source.hasHeader() && source.getHeader().hasOrderId()) {
            return UUID.fromString(source.getHeader().getOrderId().getValue());
        } else {
            return null;
        }
    }
}
