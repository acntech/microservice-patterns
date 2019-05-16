package no.acntech.shipping.resource;

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
        // returns 500 in order to trigger retry functionality in order service
        LOGGER.info("Received call - returning 500");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
