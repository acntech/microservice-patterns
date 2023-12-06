package no.acntech.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServletBearerExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration(proxyBeanMethods = false)
public class ApplicationConfig {

    @Bean
    public WebClient webClient(final WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .filter(new ServletBearerExchangeFilterFunction())
                .build();
    }
}
