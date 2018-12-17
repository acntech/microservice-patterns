package no.acntech.shipping.resource;

import brave.ScopedSpan;
import brave.Tracer;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("shipments")
public class ShippingResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShippingResource.class);

    private final Tracer tracer;

    @Autowired
    public ShippingResource(Tracer tracer) {
        this.tracer = tracer;
    }

    @PostMapping
    public ResponseEntity ship(@RequestBody String orderId) {
        // starting span for OrderResource#submit method
        ScopedSpan scopedSpan = tracer.startScopedSpan("ShippingResource#ship");
        try {
            LOGGER.debug("ShippingResource#ship for orderId={}", orderId);
            String shippingId = UUID.randomUUID().toString();
            LOGGER.info("Order with orderId={} shipped with shippingId={}", orderId, shippingId);
            return ResponseEntity.ok(shippingId);
        } finally {
            scopedSpan.finish();
        }
    }

}

