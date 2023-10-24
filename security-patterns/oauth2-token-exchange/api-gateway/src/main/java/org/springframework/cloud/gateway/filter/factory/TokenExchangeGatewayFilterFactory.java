package org.springframework.cloud.gateway.filter.factory;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthorizationContextAttribute;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

public class TokenExchangeGatewayFilterFactory extends AbstractGatewayFilterFactory<TokenExchangeGatewayFilterFactory.FilterConfig> {

    private final ObjectProvider<ReactiveOAuth2AuthorizedClientManager> clientManagerProvider;

    public TokenExchangeGatewayFilterFactory(final ObjectProvider<ReactiveOAuth2AuthorizedClientManager> clientManagerProvider) {
        super(FilterConfig.class);
        this.clientManagerProvider = clientManagerProvider;
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return FilterConfig.shortcutFieldOrder();
    }

    @Override
    public GatewayFilter apply(final FilterConfig config) {
        return (exchange, chain) -> exchange.getPrincipal()
                //.log(TokenExchangeGatewayFilterFactory.class.getCanonicalName())
                .filter(principal -> principal instanceof OAuth2AuthenticationToken)
                .cast(OAuth2AuthenticationToken.class)
                .flatMap(token -> this.authorizedClient(config, token))
                .map(OAuth2AuthorizedClient::getAccessToken)
                .map(token -> withBearerAuth(exchange, token))
                .defaultIfEmpty(exchange)
                .flatMap(chain::filter);
    }

    private Mono<OAuth2AuthorizedClient> authorizedClient(final FilterConfig config,
                                                          final OAuth2AuthenticationToken authentication) {
        final var clientRegistrationId = authentication.getAuthorizedClientRegistrationId();
        final var request = OAuth2AuthorizeRequest.withClientRegistrationId(clientRegistrationId)
                .principal(authentication)
                .attribute(OAuth2AuthorizationContextAttribute.AUDIENCE_ATTRIBUTE_NAME, config.audience)
                .attribute(OAuth2AuthorizationContextAttribute.SCOPES_ATTRIBUTE_NAME, config.scopes)
                .build();
        final var clientManager = clientManagerProvider.getIfAvailable();
        if (clientManager == null) {
            return Mono.error(new IllegalStateException("No ReactiveOAuth2AuthorizedClientManager bean was found"));
        }
        return clientManager.authorize(request);
    }

    private ServerWebExchange withBearerAuth(final ServerWebExchange exchange,
                                             final OAuth2AccessToken accessToken) {
        return exchange.mutate()
                .request(r -> r.headers(headers -> headers.setBearerAuth(accessToken.getTokenValue())))
                .build();
    }

    public static class FilterConfig {

        private static final String AUDIENCE = "audience";
        private static final String SCOPES = "scopes";

        public static List<String> shortcutFieldOrder() {
            return Arrays.asList(AUDIENCE, SCOPES);
        }

        private String audience;
        private String scopes;

        public String getAudience() {
            return audience;
        }

        public void setAudience(String audience) {
            this.audience = audience;
        }

        public String getScopes() {
            return scopes;
        }

        public void setScopes(String scopes) {
            this.scopes = scopes;
        }
    }
}
