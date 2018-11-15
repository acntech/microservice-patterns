package no.acntech.order.resource;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.acntech.order.model.Order;
import no.acntech.order.service.OrderService;

@RequestMapping(path = "orders")
@RestController
public class OrderResource {

    private final OrderService orderService;

    public OrderResource(final OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> get() {
        return orderService.findOrders();
    }
}
