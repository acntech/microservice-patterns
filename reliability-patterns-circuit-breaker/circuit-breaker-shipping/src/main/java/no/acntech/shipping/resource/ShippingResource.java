package no.acntech.shipping.resource;

import java.time.Duration;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("shipments")
public class ShippingResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShippingResource.class);

    @PostMapping
    public ResponseEntity ship(@RequestBody String orderId) {
        // Trigger error-situations in order to demonstrate circuit breaker upstream
        if (orderId.toLowerCase().endsWith("3") || orderId.toLowerCase().endsWith("6")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } else if (orderId.toLowerCase().endsWith("5") || orderId.toLowerCase().endsWith("9")) {
            try {
                Thread.sleep(Duration.ofSeconds(10).toMillis());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        String shippingId = UUID.randomUUID().toString();
        LOGGER.info("Order with orderId={} shipped with shippingId={}", orderId, shippingId);
        return ResponseEntity.ok(shippingId);
    }
}
