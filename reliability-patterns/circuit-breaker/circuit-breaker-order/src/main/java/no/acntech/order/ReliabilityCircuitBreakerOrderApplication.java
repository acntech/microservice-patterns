package no.acntech.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
@EnableCircuitBreaker
@EnableHystrixDashboard
public class ReliabilityCircuitBreakerOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReliabilityCircuitBreakerOrderApplication.class, args);
    }
}
