package no.acntech.config;

import org.springframework.cloud.gateway.filter.factory.TokenExchangeGatewayFilterFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;

@Configuration(proxyBeanMethods = false)
public class GatewayFilterConfig {

    @Bean
    public TokenExchangeGatewayFilterFactory tokenExchangeGatewayFilterFactory(
            final ObjectProvider<ReactiveOAuth2AuthorizedClientManager> clientManagerProvider) {
        return new TokenExchangeGatewayFilterFactory(clientManagerProvider);
    }
}
