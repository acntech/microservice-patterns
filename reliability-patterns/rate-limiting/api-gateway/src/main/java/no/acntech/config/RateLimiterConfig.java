package no.acntech.config;

import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.grid.jcache.JCacheProxyManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Cache;

@Configuration
public class RateLimiterConfig {

    @Bean
    public ProxyManager<String> proxyManager(final Cache<String, byte[]> cache) {
        return new JCacheProxyManager<>(cache);
    }
}
