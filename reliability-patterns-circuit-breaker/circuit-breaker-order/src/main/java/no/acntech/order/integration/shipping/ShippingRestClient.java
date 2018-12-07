package no.acntech.order.integration.shipping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import no.acntech.order.entity.Order;

@Component
public class ShippingRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShippingRestClient.class);

    private final RestTemplate restTemplate;

    @Autowired
    public ShippingRestClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .build();
    }

    /**
     * List of hystrix command properties: https://github.com/Netflix/Hystrix/wiki/Configuration#CommandExecution
     * <p>
     * The following configuration specifies:
     * - Timeout and error if call takes more than 2 seconds.
     * - If we in 10 seconds (metrics.rollingStats.timeInMilliseconds) receive at least 5 requests (circuitBreaker.requestVolumeThreshold)
     * - Check if 50% or more has failed (curcuitBreaker.errorThresholdPercentage)
     * -- If < 50%, let the circuit stay closed (call downstream)
     * -- If > 50%, open the circuit for 10 seconds (curcuitBreaker.sleepWindowInMilliseconds). After that, let the first request be served on closed circuit (call downstream)
     * ---- If the request is OK, close the circuit and start measuring again
     * ---- If first request fails, keep the circuit open for 10 more seconds and retry
     */
    @HystrixCommand(fallbackMethod = "shipFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"), // timeout
            @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000"), // time window to collect information
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"), // number of requests in time window required to measure
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"), // if requestVolumeThreshold is reached, this is the error percentage before tripping the circuit
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), // time to wait after tripping the circuit
    })
    public String ship(Order order) {
        ResponseEntity<String> shippingIdResponseEntity = restTemplate.postForEntity("http://localhost:8082/shipments", order.getId(), String.class);
        return shippingIdResponseEntity.getBody();
    }

    public String shipFallback(Order order) {
        LOGGER.debug("BACKUP!");
        return "-1";
    }
}
