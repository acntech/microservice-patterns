package no.acntech.order.converter;

import no.acntech.order.model.FindOrderItemsRequest;
import no.acntech.order.model.OrderItemQuery;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FindOrderItemsRequestToOrderItemQueryConverter implements Converter<FindOrderItemsRequest, OrderItemQuery> {

    @NonNull
    @Override
    public OrderItemQuery convert(@NonNull final FindOrderItemsRequest source) {
        return OrderItemQuery.build()
                .orderId(source.getHeader().hasOrderId() ? UUID.fromString(source.getHeader().getOrderId().getValue()) : null)
                .build();
    }
}
