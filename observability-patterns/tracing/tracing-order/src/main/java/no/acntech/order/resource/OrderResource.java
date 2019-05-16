package no.acntech.order.resource;

import brave.ScopedSpan;
import brave.Tracer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderResource.class);

    private final OrderService orderService;
    private final Tracer tracer;

    @Autowired
    public OrderResource(OrderService orderService,
                         Tracer tracer) {
        this.orderService = orderService;
        this.tracer = tracer;
    }

    @PostMapping
    public ResponseEntity submit(@RequestBody Order order) {
        ScopedSpan scopedSpan = tracer.startScopedSpan("OrderResource#submit");
        try {
            LOGGER.debug("OrderResource#submit for order: {}", order.toString());
            Order createdOrder = orderService.submit(order);

            LOGGER.debug("Order successfully created, responding with HttpStatus={}", HttpStatus.OK);
            return ResponseEntity.ok(createdOrder);
        } finally {
            scopedSpan.finish();
        }
    }

    @GetMapping
    public ResponseEntity orders() {
        return ResponseEntity.ok(orderService.findAllOrders());
    }

}
