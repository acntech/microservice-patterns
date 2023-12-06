package org.springframework.security.oauth2.core;

import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;

public class OAuth2AuthorizationContextAttributesMapper implements Function<OAuth2AuthorizeRequest, Mono<Map<String, Object>>> {

    @Override
    public Mono<Map<String, Object>> apply(final OAuth2AuthorizeRequest oAuth2AuthorizeRequest) {
        return Mono.just(oAuth2AuthorizeRequest.getAttributes());
    }
}
