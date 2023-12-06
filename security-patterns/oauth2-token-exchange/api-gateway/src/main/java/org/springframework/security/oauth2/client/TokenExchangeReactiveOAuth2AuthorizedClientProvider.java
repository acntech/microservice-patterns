/*
 * Copyright 2002-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.security.oauth2.client;

import org.springframework.security.oauth2.client.endpoint.OAuth2TokenExchangeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.ReactiveOAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.WebClientReactiveTokenExchangeTokenResponseClient;
import org.springframework.security.oauth2.core.OAuth2AuthorizationContextAttribute;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashSet;

public final class TokenExchangeReactiveOAuth2AuthorizedClientProvider implements ReactiveOAuth2AuthorizedClientProvider {

    private final ReactiveOAuth2AccessTokenResponseClient<OAuth2TokenExchangeGrantRequest> accessTokenResponseClient;

    public TokenExchangeReactiveOAuth2AuthorizedClientProvider() {
        this.accessTokenResponseClient = new WebClientReactiveTokenExchangeTokenResponseClient();
    }

    @Override
    public Mono<OAuth2AuthorizedClient> authorize(final OAuth2AuthorizationContext context) {
        Assert.notNull(context, "Context cannot be null");

        final var clientRegistration = context.getClientRegistration();
        final var authorizedClient = context.getAuthorizedClient();
        if (authorizedClient == null || authorizedClient.getAccessToken() == null) {
            return Mono.empty();
        }

        final var audienceAttribute = context.getAttribute(OAuth2AuthorizationContextAttribute.AUDIENCE_ATTRIBUTE_NAME);
        if (!(audienceAttribute instanceof String)) {
            return Mono.empty();
        }
        final var audience = audienceAttribute.toString();

        final var scopeAttribute = context.getAttribute(OAuth2AuthorizationContextAttribute.SCOPES_ATTRIBUTE_NAME);
        final var scopes = scopeAttribute == null ? new HashSet<String>() : new HashSet<>(Arrays.asList(scopeAttribute.toString().trim().split("\\s+")));

        final var accessToken = authorizedClient.getAccessToken();

        return Mono.just(new OAuth2TokenExchangeGrantRequest(clientRegistration, accessToken, audience, scopes))
                .flatMap(this.accessTokenResponseClient::getTokenResponse)
                .onErrorMap(OAuth2AuthorizationException.class,
                        (ex) -> new ClientAuthorizationException(ex.getError(), clientRegistration.getRegistrationId(), ex))
                .map((tokenResponse) -> new OAuth2AuthorizedClient(clientRegistration, context.getPrincipal().getName(),
                        tokenResponse.getAccessToken()));
    }
}
