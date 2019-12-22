package no.acntech.config;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.support.ConfigurationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.validation.Validator;

import no.acntech.factory.ThrottlingRequestCounterFactory;
import no.acntech.limiter.ThrottlingRateLimiter;
import no.acntech.resolver.HeaderKeyResolver;

@EnableConfigurationProperties({
        GatewayProperties.class,
        HazelcastProperties.class
})
@Configuration
public class GatewayConfig {

    @Bean
    public HazelcastClusterInitializer hazelcastClusterInitializer(final HazelcastProperties hazelcastProperties) {
        return new HazelcastClusterInitializer(hazelcastProperties.getName(), hazelcastProperties.getHosts());
    }

    @Bean
    public ThrottlingRequestCounterFactory throttlingRequestCounterFactory(final HazelcastClusterInitializer hazelcastClusterInitializer) {
        return new ThrottlingRequestCounterFactory(hazelcastClusterInitializer);
    }

    @Bean
    public ThrottlingRateLimiter throttlingRateLimiter(final ConfigurationService configurationService,
                                                       final GatewayProperties gatewayProperties,
                                                       final ThrottlingRequestCounterFactory throttlingRequestCounterFactory) {
        return new ThrottlingRateLimiter(configurationService, gatewayProperties, throttlingRequestCounterFactory);
    }

    @Bean
    public HeaderKeyResolver headerKeyResolver(final GatewayProperties gatewayProperties) {
        return new HeaderKeyResolver(gatewayProperties.getApiKeyHeader());
    }

    @Bean
    public ConfigurationService configurationService(final BeanFactory beanFactory,
                                                     final ConversionService conversionService,
                                                     final Validator defaultValidator) {
        return new ConfigurationService(beanFactory, conversionService, defaultValidator);
    }
}
