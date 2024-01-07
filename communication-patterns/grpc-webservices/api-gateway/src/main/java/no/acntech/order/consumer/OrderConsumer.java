package no.acntech.order.consumer;

import jakarta.validation.constraints.NotNull;
import net.devh.boot.grpc.client.inject.GrpcClient;
import no.acntech.order.endpoint.OrderEndpointGrpc;
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
public class OrderConsumer {

    @Autowired
    private ConversionService conversionService;
    @GrpcClient("ordering-service")
    private OrderEndpointGrpc.OrderEndpointBlockingStub blockingStub;


    public Mono<OrderDto> getOrder(@NotNull final UUID orderId) {
        return Mono.just(orderId)
                .map(this::convertGetRequest)
                .publishOn(Schedulers.boundedElastic())
                .map(blockingStub::getOrder)
                .map(GetOrderResponse::getBody);
    }

    public Flux<OrderDto> findOrders(@NotNull final OrderQuery orderQuery) {
        return Mono.just(orderQuery)
                .map(this::convertFindRequest)
                .publishOn(Schedulers.boundedElastic())
                .map(blockingStub::findOrders)
                .map(FindOrdersResponse::getBodyList)
                .flatMapMany(Flux::fromIterable);
    }

    public Mono<OrderDto> createOrder(@NotNull final CreateOrderDto createOrder) {
        return Mono.just(createOrder)
                .map(this::convertCreateRequest)
                .publishOn(Schedulers.boundedElastic())
                .map(blockingStub::createOrder)
                .map(CreateOrderResponse::getBody);
    }

    public Mono<OrderDto> updateOrder(@NotNull final UUID orderId) {
        return Mono.just(orderId)
                .map(this::convertUpdateRequest)
                .publishOn(Schedulers.boundedElastic())
                .map(blockingStub::updateOrder)
                .map(UpdateOrderResponse::getBody);
    }

    public Mono<OrderDto> deleteOrder(@NotNull final UUID orderId) {
        return Mono.just(orderId)
                .map(this::convertDeleteRequest)
                .publishOn(Schedulers.boundedElastic())
                .map(blockingStub::deleteOrder)
                .map(DeleteOrderResponse::getBody);
    }

    private GetOrderRequest convertGetRequest(final UUID source) {
        return Objects.requireNonNull(conversionService.convert(source, GetOrderRequest.class));
    }

    private FindOrdersRequest convertFindRequest(final OrderQuery source) {
        return Objects.requireNonNull(conversionService.convert(source, FindOrdersRequest.class));
    }

    private CreateOrderRequest convertCreateRequest(final CreateOrderDto source) {
        return Objects.requireNonNull(conversionService.convert(source, CreateOrderRequest.class));
    }

    private UpdateOrderRequest convertUpdateRequest(final UUID source) {
        return Objects.requireNonNull(conversionService.convert(source, UpdateOrderRequest.class));
    }

    private DeleteOrderRequest convertDeleteRequest(final UUID source) {
        return Objects.requireNonNull(conversionService.convert(source, DeleteOrderRequest.class));
    }
}
