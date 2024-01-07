package no.acntech.order.converter;

import no.acntech.order.model.FindOrdersRequest;
import no.acntech.order.model.OrderQuery;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FindOrdersRequestToOrderQueryConverter implements Converter<FindOrdersRequest, OrderQuery> {

    @NonNull
    @Override
    public OrderQuery convert(@NonNull final FindOrdersRequest source) {
        return OrderQuery.build()
                .customerId(source.getHeader().hasCustomerId() ? UUID.fromString(source.getHeader().getCustomerId().getValue()) : null)
                .status(source.getHeader().getStatus())
                .build();
    }
}
