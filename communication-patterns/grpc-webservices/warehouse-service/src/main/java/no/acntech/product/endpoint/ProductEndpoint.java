package no.acntech.product.endpoint;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import no.acntech.common.error.GrpcErrorHandler;
import no.acntech.product.model.*;
import no.acntech.product.service.ProductService;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;

import java.util.UUID;

@GrpcService
public class ProductEndpoint extends ProductEndpointGrpc.ProductEndpointImplBase {

    private final ConversionService conversionService;
    private final GrpcErrorHandler errorHandler;
    private final ProductService productService;

    public ProductEndpoint(final ConversionService conversionService,
                           final GrpcErrorHandler errorHandler,
                           final ProductService productService) {
        this.conversionService = conversionService;
        this.errorHandler = errorHandler;
        this.productService = productService;
    }

    @Override
    public void getProduct(final GetProductRequest request,
                           final StreamObserver<GetProductResponse> responseObserver) {
        try {
            final var productDto = productService.getProduct(UUID.fromString(request.getHeader().getProductId().getValue()));
            final var response = GetProductResponse.newBuilder()
                    .setBody(productDto)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            final var statusException = errorHandler.onError(e);
            responseObserver.onError(statusException);
        }
    }

    @Override
    public void findProducts(final FindProductsRequest request,
                             final StreamObserver<FindProductsResponse> responseObserver) {
        try {
            final var query = conversionService.convert(request, ProductQuery.class);
            Assert.notNull(query, "Failed to convert FindProductsRequest to ProductQuery");
            final var productDtos = productService.findProducts(query);
            final var response = FindProductsResponse.newBuilder()
                    .addAllBody(productDtos)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            final var statusException = errorHandler.onError(e);
            responseObserver.onError(statusException);
        }
    }

    @Override
    public void createProduct(final CreateProductRequest request,
                              final StreamObserver<CreateProductResponse> responseObserver) {
        try {
            final var productDto = productService.createProduct(request.getBody());
            final var response = CreateProductResponse.newBuilder()
                    .setBody(productDto)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            final var statusException = errorHandler.onError(e);
            responseObserver.onError(statusException);
        }
    }
}
