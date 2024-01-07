package no.acntech.order.endpoint;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import no.acntech.common.error.GrpcErrorHandler;
import no.acntech.order.model.*;
import no.acntech.order.service.OrderOrchestrationService;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;

import java.util.UUID;

@GrpcService
public class OrderEndpoint extends OrderEndpointGrpc.OrderEndpointImplBase {

    private final ConversionService conversionService;
    private final GrpcErrorHandler errorHandler;
    private final OrderOrchestrationService orderOrchestrationService;

    public OrderEndpoint(final ConversionService conversionService,
                         final GrpcErrorHandler errorHandler,
                         final OrderOrchestrationService orderOrchestrationService) {
        this.conversionService = conversionService;
        this.errorHandler = errorHandler;
        this.orderOrchestrationService = orderOrchestrationService;
    }

    @Override
    public void getOrder(final GetOrderRequest request,
                         final StreamObserver<GetOrderResponse> responseObserver) {
        try {
            final var orderId = conversionService.convert(request, UUID.class);
            Assert.notNull(orderId, "Failed to convert GetOrderRequest to UUID");
            final var orderDto = orderOrchestrationService.getOrder(orderId);
            final var response = GetOrderResponse.newBuilder()
                    .setBody(orderDto)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            final var statusException = errorHandler.onError(e);
            responseObserver.onError(statusException);
        }
    }

    @Override
    public void findOrders(final FindOrdersRequest request,
                           final StreamObserver<FindOrdersResponse> responseObserver) {
        try {
            final var query = conversionService.convert(request, OrderQuery.class);
            Assert.notNull(query, "Failed to convert FindOrdersRequest to OrderQuery");
            final var orderDtos = orderOrchestrationService.findOrders(query);
            final var response = FindOrdersResponse.newBuilder()
                    .addAllBody(orderDtos)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            final var statusException = errorHandler.onError(e);
            responseObserver.onError(statusException);
        }
    }

    @Override
    public void createOrder(final CreateOrderRequest request,
                            final StreamObserver<CreateOrderResponse> responseObserver) {
        try {
            final var orderDto = orderOrchestrationService.createOrder(request.getBody());
            final var response = CreateOrderResponse.newBuilder()
                    .setBody(orderDto)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            final var statusException = errorHandler.onError(e);
            responseObserver.onError(statusException);
        }
    }

    @Override
    public void updateOrder(final UpdateOrderRequest request,
                            final StreamObserver<UpdateOrderResponse> responseObserver) {
        try {
            final var orderDto = orderOrchestrationService.updateOrder(UUID.fromString(request.getHeader().getOrderId().getValue()));
            final var response = UpdateOrderResponse.newBuilder()
                    .setBody(orderDto)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            final var statusException = errorHandler.onError(e);
            responseObserver.onError(statusException);
        }
    }

    @Override
    public void deleteOrder(final DeleteOrderRequest request,
                            final StreamObserver<DeleteOrderResponse> responseObserver) {
        try {
            final var orderDto = orderOrchestrationService.deleteOrder(UUID.fromString(request.getHeader().getOrderId().getValue()));
            final var response = DeleteOrderResponse.newBuilder()
                    .setBody(orderDto)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            final var statusException = errorHandler.onError(e);
            responseObserver.onError(statusException);
        }
    }
}
