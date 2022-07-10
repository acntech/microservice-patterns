package no.acntech.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;

@Configuration
public class CacheConfig {

    private static final String CACHE_NAME = "rate-limiter";

    @Bean
    public CacheManager cacheManager() {
        final var provider = Caching.getCachingProvider();
        return provider.getCacheManager();
    }

    @Bean
    public Cache<String, byte[]> cache(final CacheManager cacheManager) {
        final var config = new MutableConfiguration<String, byte[]>();
        return cacheManager.createCache(CACHE_NAME, config);
    }
}
