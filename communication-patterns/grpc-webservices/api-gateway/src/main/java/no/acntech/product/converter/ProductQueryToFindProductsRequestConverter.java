package no.acntech.product.converter;

import com.google.protobuf.StringValue;
import no.acntech.product.model.FindProductsHeader;
import no.acntech.product.model.FindProductsRequest;
import no.acntech.product.model.ProductQuery;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ProductQueryToFindProductsRequestConverter implements Converter<ProductQuery, FindProductsRequest> {

    @NonNull
    @Override
    public FindProductsRequest convert(@NonNull final ProductQuery source) {
        return FindProductsRequest.newBuilder()
                .setHeader(convertHeader(source))
                .build();
    }

    private FindProductsHeader convertHeader(final ProductQuery source) {
        final var builder = FindProductsHeader.newBuilder();
        if (source.getName() != null) {
            builder.setName(StringValue.of(source.getName()));
        }
        return builder.build();
    }
}
