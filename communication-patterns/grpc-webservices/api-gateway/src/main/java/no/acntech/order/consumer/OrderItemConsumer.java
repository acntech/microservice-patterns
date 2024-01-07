package no.acntech.order.consumer;

import groovy.lang.Tuple2;
import jakarta.validation.constraints.NotNull;
import net.devh.boot.grpc.client.inject.GrpcClient;
import no.acntech.order.endpoint.OrderItemEndpointGrpc;
import no.acntech.order.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;
import java.util.UUID;

@Component
public class OrderItemConsumer {


    @Autowired
    private ConversionService conversionService;
    @GrpcClient("ordering-service")
    private OrderItemEndpointGrpc.OrderItemEndpointBlockingStub blockingStub;


    public Mono<OrderItemDto> getOrderItem(@NotNull final UUID orderItemId) {
        return Mono.just(orderItemId)
                .map(this::convertGetRequest)
                .publishOn(Schedulers.boundedElastic())
                .map(blockingStub::getOrderItem)
                .map(GetOrderItemResponse::getBody);
    }

    public Flux<OrderItemDto> findOrderItems(@NotNull final OrderQuery orderQuery) {
        return Mono.just(orderQuery)
                .map(this::convertFindRequest)
                .publishOn(Schedulers.boundedElastic())
                .map(blockingStub::findOrderItems)
                .map(FindOrderItemsResponse::getBodyList)
                .flatMapMany(Flux::fromIterable);
    }

    public Mono<OrderDto> createOrderItem(@NotNull final UUID orderId,
                                          @NotNull final CreateOrderItemDto createOrderItemDto) {
        return Mono.just(Tuple2.tuple(orderId, createOrderItemDto))
                .map(this::convertCreateRequest)
                .publishOn(Schedulers.boundedElastic())
                .map(blockingStub::createOrderItem)
                .map(CreateOrderItemResponse::getBody);
    }

    public Mono<OrderDto> updateOrderItem(@NotNull final UUID orderItemId,
                                          @NotNull final UpdateOrderItemDto updateOrderItemDto) {
        return Mono.just(Tuple2.tuple(orderItemId, updateOrderItemDto))
                .map(this::convertUpdateRequest)
                .publishOn(Schedulers.boundedElastic())
                .map(blockingStub::updateOrderItem)
                .map(UpdateOrderItemResponse::getBody);
    }

    public Mono<OrderDto> deleteOrderItem(@NotNull final UUID orderItemId) {
        return Mono.just(orderItemId)
                .map(this::convertDeleteRequest)
                .publishOn(Schedulers.boundedElastic())
                .map(blockingStub::deleteOrderItem)
                .map(DeleteOrderItemResponse::getBody);
    }

    private GetOrderItemRequest convertGetRequest(final UUID source) {
        return Objects.requireNonNull(conversionService.convert(source, GetOrderItemRequest.class));
    }

    private FindOrderItemsRequest convertFindRequest(final OrderQuery source) {
        return Objects.requireNonNull(conversionService.convert(source, FindOrderItemsRequest.class));
    }

    private CreateOrderItemRequest convertCreateRequest(final Tuple2<UUID, CreateOrderItemDto> source) {
        return Objects.requireNonNull(conversionService.convert(source, CreateOrderItemRequest.class));
    }

    private UpdateOrderItemRequest convertUpdateRequest(final Tuple2<UUID, UpdateOrderItemDto> source) {
        return Objects.requireNonNull(conversionService.convert(source, UpdateOrderItemRequest.class));
    }

    private DeleteOrderItemRequest convertDeleteRequest(final UUID source) {
        return Objects.requireNonNull(conversionService.convert(source, DeleteOrderItemRequest.class));
    }
}
