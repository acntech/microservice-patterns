package no.acntech.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration(proxyBeanMethods = false)
public class WebClientConfig {

    @Bean
    public WebClient webClient(final WebClient.Builder webClientBuilder) {
        return webClientBuilder.build();
    }
}
