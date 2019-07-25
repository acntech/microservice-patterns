package no.acntech.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator defaultRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(proxy -> proxy
                        .path("/api/customers/**")
                        .uri("http://localhost:9000"))
                .route(proxy -> proxy
                        .path("/api/orders/**", "/api/items/**")
                        .uri("http://localhost:9010"))
                .route(proxy -> proxy
                        .path("/api/products/**", "/api/reservations/**")
                        .uri("http://localhost:9040"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> testWhenMetricPathIsNotMeet() {
        return RouterFunctions.route(path("/api/login"),
                request -> ServerResponse.status(HttpStatus.NOT_IMPLEMENTED).build());
    }
}
