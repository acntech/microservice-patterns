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
public class OrderItemEndpoint extends OrderItemEndpointGrpc.OrderItemEndpointImplBase {

    private final ConversionService conversionService;
    private final GrpcErrorHandler errorHandler;
    private final OrderOrchestrationService orderOrchestrationService;

    public OrderItemEndpoint(final ConversionService conversionService,
                             final GrpcErrorHandler errorHandler,
                             final OrderOrchestrationService orderOrchestrationService) {
        this.conversionService = conversionService;
        this.errorHandler = errorHandler;
        this.orderOrchestrationService = orderOrchestrationService;
    }

    @Override
    public void getOrderItem(final GetOrderItemRequest request,
                             final StreamObserver<GetOrderItemResponse> responseObserver) {
        try {
            final var orderItemDto = orderOrchestrationService.getOrderItem(UUID.fromString(request.getHeader().getItemId().getValue()));
            final var response = GetOrderItemResponse.newBuilder()
                    .setBody(orderItemDto)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            final var statusException = errorHandler.onError(e);
            responseObserver.onError(statusException);
        }
    }

    @Override
    public void findOrderItems(final FindOrderItemsRequest request,
                               final StreamObserver<FindOrderItemsResponse> responseObserver) {
        try {
            final var query = conversionService.convert(request, OrderItemQuery.class);
            Assert.notNull(query, "Failed to convert FindOrderItemsRequest to OrderItemQuery");
            final var orderItemDtos = orderOrchestrationService.findOrderItems(query);
            final var response = FindOrderItemsResponse.newBuilder()
                    .addAllBody(orderItemDtos)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            final var statusException = errorHandler.onError(e);
            responseObserver.onError(statusException);
        }
    }

    @Override
    public void createOrderItem(final CreateOrderItemRequest request,
                                final StreamObserver<CreateOrderItemResponse> responseObserver) {
        try {
            final var orderDto = orderOrchestrationService.createOrderItem(
                    UUID.fromString(request.getHeader().getOrderId().getValue()),
                    request.getBody());
            final var response = CreateOrderItemResponse.newBuilder()
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
    public void updateOrderItem(final UpdateOrderItemRequest request,
                                final StreamObserver<UpdateOrderItemResponse> responseObserver) {
        try {
            final var orderDto = orderOrchestrationService.updateOrderItem(
                    UUID.fromString(request.getHeader().getItemId().getValue()),
                    request.getBody());
            final var response = UpdateOrderItemResponse.newBuilder()
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
    public void deleteOrderItem(final DeleteOrderItemRequest request,
                                final StreamObserver<DeleteOrderItemResponse> responseObserver) {
        try {
            final var orderDto = orderOrchestrationService.deleteOrderItem(UUID.fromString(request.getHeader().getItemId().getValue()));
            final var response = DeleteOrderItemResponse.newBuilder()
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
