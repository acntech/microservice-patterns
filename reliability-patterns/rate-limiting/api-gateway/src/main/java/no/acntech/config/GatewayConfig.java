package no.acntech.config;

import no.acntech.factory.DefaultRequestCounterFactory;
import no.acntech.factory.RequestCounterFactory;
import no.acntech.limiter.DefaultRateLimiter;
import no.acntech.limiter.RateLimiterConfig;
import no.acntech.resolver.HeaderKeyResolver;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.support.ConfigurationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.validation.Validator;

@ConfigurationPropertiesScan
@Configuration
public class GatewayConfig {

    @Bean
    public HazelcastClusterInitializer hazelcastClusterInitializer(final HazelcastProperties hazelcastProperties) {
        return new HazelcastClusterInitializer(hazelcastProperties.getName(), hazelcastProperties.getHosts());
    }

    @Bean
    public RequestCounterFactory requestCounterFactory(final HazelcastClusterInitializer hazelcastClusterInitializer) {
        return new DefaultRequestCounterFactory(hazelcastClusterInitializer);
    }

    @Bean
    public RateLimiter<RateLimiterConfig> rateLimiter(final ConfigurationService configurationService,
                                                      final RequestCounterFactory requestCounterFactory) {
        return new DefaultRateLimiter(configurationService, requestCounterFactory);
    }

    @Bean
    public KeyResolver keyResolver() {
        return new HeaderKeyResolver("X-API-Key");
    }

    @Bean
    public ConfigurationService configurationService(final BeanFactory beanFactory,
                                                     final ConversionService conversionService,
                                                     final Validator defaultValidator) {
        return new ConfigurationService(beanFactory, conversionService, defaultValidator);
    }
}
