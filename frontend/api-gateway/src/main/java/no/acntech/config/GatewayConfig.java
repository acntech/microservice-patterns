package no.acntech.config;

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
    public RouterFunction<ServerResponse> testWhenMetricPathIsNotMeet() {
        return RouterFunctions.route(path("/api/login"),
                request -> ServerResponse.status(HttpStatus.NOT_IMPLEMENTED).build());
    }
}
