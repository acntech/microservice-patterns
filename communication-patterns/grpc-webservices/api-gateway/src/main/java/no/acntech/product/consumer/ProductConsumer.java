package no.acntech.product.consumer;

import jakarta.validation.constraints.NotNull;
import net.devh.boot.grpc.client.inject.GrpcClient;
import no.acntech.product.endpoint.ProductEndpointGrpc;
import no.acntech.product.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;
import java.util.UUID;

@Component
public class ProductConsumer {

    @Autowired
    private ConversionService conversionService;
    @GrpcClient("warehouse-service")
    private ProductEndpointGrpc.ProductEndpointBlockingStub blockingStub;


    public Mono<ProductDto> getProduct(@NotNull final UUID orderId) {
        return Mono.just(orderId)
                .map(this::convertGetRequest)
                .publishOn(Schedulers.boundedElastic())
                .map(blockingStub::getProduct)
                .map(GetProductResponse::getBody);
    }

    public Flux<ProductDto> findProducts(@NotNull final ProductQuery productQuery) {
        return Mono.just(productQuery)
                .map(this::convertFindRequest)
                .publishOn(Schedulers.boundedElastic())
                .map(blockingStub::findProducts)
                .map(FindProductsResponse::getBodyList)
                .flatMapMany(Flux::fromIterable);
    }

    private GetProductRequest convertGetRequest(final UUID source) {
        return Objects.requireNonNull(conversionService.convert(source, GetProductRequest.class));
    }

    private FindProductsRequest convertFindRequest(final ProductQuery source) {
        return Objects.requireNonNull(conversionService.convert(source, FindProductsRequest.class));
    }
}
