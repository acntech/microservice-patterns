package no.acntech.config;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.support.ConfigurationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

    private static final String HEADER_NAME = "X-API-Key";

    @Bean
    public KeyResolver headerKeyResolver() {
        return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HEADER_NAME));
    }

    @Bean
    public ConfigurationService configurationService(final BeanFactory beanFactory,
                                                     final ConversionService conversionService,
                                                     final Validator defaultValidator) {
        return new ConfigurationService(beanFactory, () -> conversionService, () -> defaultValidator);
    }
}
