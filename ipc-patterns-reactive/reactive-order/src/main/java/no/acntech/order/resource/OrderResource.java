package no.acntech.order.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity submit(@RequestBody Order order) {
        Order createdOrder = orderService.submit(order);
        return ResponseEntity.ok(createdOrder);
    }

    @GetMapping
    public ResponseEntity orders() {
        return ResponseEntity.ok(orderService.findAllOrders());
    }

}
