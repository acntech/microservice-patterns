package no.acntech.order.converter;

import com.google.protobuf.StringValue;
import no.acntech.order.model.FindOrdersHeader;
import no.acntech.order.model.FindOrdersRequest;
import no.acntech.order.model.OrderQuery;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class OrderQueryToFindOrdersRequestConverter implements Converter<OrderQuery, FindOrdersRequest> {

    @NonNull
    @Override
    public FindOrdersRequest convert(@NonNull final OrderQuery source) {
        return FindOrdersRequest.newBuilder()
                .setHeader(convertHeader(source))
                .build();
    }

    private FindOrdersHeader convertHeader(final OrderQuery source) {
        final var builder = FindOrdersHeader.newBuilder();
        if (source.getCustomerId() != null) {
            builder.setCustomerId(StringValue.of(source.getCustomerId().toString()));
        }
        if (source.getStatus() != null) {
            builder.setStatus(source.getStatus());
        }
        return builder.build();
    }
}
