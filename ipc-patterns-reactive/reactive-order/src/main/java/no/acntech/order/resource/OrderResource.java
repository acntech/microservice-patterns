package no.acntech.order.resource;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.acntech.order.entity.Order;
import no.acntech.order.service.OrderService;

@RestController
@RequestMapping("orders")
public class OrderResource {

    private final OrderService orderService;

    @Autowired
    public OrderResource(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Mono<Order> submit(@RequestBody Order order) {
        return orderService.submit(order);
    }

    @PutMapping("/{id}")
    public Mono<Order> update(@PathVariable("id") String id, @RequestBody Order order) {
        return orderService.update(id, order);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable("id") String id) {
        return orderService.delete(id);
    }

    @GetMapping("/{id}")
    public Mono<Order> get(@PathVariable("id") String id) {
        return this.orderService.findById(id);
    }

    @GetMapping
    public Flux<Order> orders() {
        return orderService.findAllOrders();
    }

}
