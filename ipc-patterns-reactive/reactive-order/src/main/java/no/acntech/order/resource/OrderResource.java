package no.acntech.order.resource;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import no.acntech.order.entity.CreateItem;
import no.acntech.order.entity.CreateOrder;
import no.acntech.order.entity.Order;
import no.acntech.order.service.OrderService;

import static io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;

@RestController
@RequestMapping("orders")
public class OrderResource {

    private final OrderService orderService;

    @Autowired
    public OrderResource(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(consumes = APPLICATION_JSON)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Order> createOrder(@RequestBody @Valid CreateOrder request) {
        return orderService.createOrder(request);
        //        return request.bodyToMono(CreateOrder.class)
        //                .map(orderService::createOrder)
        //                .flatMap(order -> ServerResponse.created(UriComponentsBuilder.fromPath("").build().toUri())
        //                        .body(BodyInserters.fromObject(order)));
        //
        //        Mono<Order> order1 = orderService.createOrder(order);
        //        ServerResponse.created()
        //        return orderService.createOrder(order)
        //                .flatMap(o -> Mono.justOrEmpty(ResponseEntity.created(UriComponentsBuilder.fromPath("orders/" + o.getId()).build().toUri()).build()));
    }

    @PostMapping(path = "{orderId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Order> postItem(@PathVariable("orderId") String orderId,
                                @RequestBody @Valid CreateItem createItem) {
        return orderService.addItem(orderId, createItem);
    }

    //    @PutMapping("/{id}")
    //    public Mono<Order> update(@PathVariable("id") String id, @RequestBody Order order) {
    //        return orderService.update(id, order);
    //    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable("id") String id) {
        return orderService.delete(id);
    }

    @GetMapping("/{id}")
    public Mono<Order> get(@PathVariable("id") String id) {
        return this.orderService.findById(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Order> orders() {
        return orderService.findAllOrders();
    }

}
