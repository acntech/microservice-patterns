package no.acntech.product.converter;

import no.acntech.product.model.FindProductsRequest;
import no.acntech.product.model.ProductQuery;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class FindProductsRequestToProductQueryConverter implements Converter<FindProductsRequest, ProductQuery> {

    @NonNull
    @Override
    public ProductQuery convert(@NonNull final FindProductsRequest source) {
        if (source.getHeader().hasName()) {
            return ProductQuery.builder()
                    .name(source.getHeader().getName().getValue())
                    .build();
        } else {
            return ProductQuery.builder()
                    .build();
        }
    }
}
