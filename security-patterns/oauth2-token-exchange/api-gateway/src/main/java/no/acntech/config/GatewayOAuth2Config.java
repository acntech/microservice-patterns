package no.acntech.config;

import org.springframework.cloud.gateway.filter.factory.TokenExchangeGatewayFilterFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.cloud.gateway.config.conditional.ConditionalOnEnabledFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.TokenExchangeReactiveOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2AuthorizationContextAttributesMapper;

@ConditionalOnEnabledFilter(TokenExchangeGatewayFilterFactory.class)
@AutoConfigureAfter(ReactiveSecurityAutoConfiguration.class)
@Configuration(proxyBeanMethods = false)
public class GatewayOAuth2Config {

    @Bean
    public ReactiveOAuth2AuthorizedClientManager gatewayReactiveOAuth2AuthorizedClientManager(
            final ReactiveClientRegistrationRepository clientRegistrationRepository,
            final ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
        final var authorizedClientProvider = ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                .authorizationCode()
                .refreshToken()
                .provider(new TokenExchangeReactiveOAuth2AuthorizedClientProvider())
                .build();
        final var authorizedClientManager = new DefaultReactiveOAuth2AuthorizedClientManager(
                clientRegistrationRepository, authorizedClientRepository);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        authorizedClientManager.setContextAttributesMapper(new OAuth2AuthorizationContextAttributesMapper());
        return authorizedClientManager;
    }
}
