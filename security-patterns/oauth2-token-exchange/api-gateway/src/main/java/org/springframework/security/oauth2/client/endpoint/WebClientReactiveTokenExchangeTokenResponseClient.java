package org.springframework.security.oauth2.client.endpoint;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.endpoint.ExtendedOAuth2ParameterNames;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Set;

public class WebClientReactiveTokenExchangeTokenResponseClient
        extends AbstractWebClientReactiveOAuth2AccessTokenResponseClient<OAuth2TokenExchangeGrantRequest> {

    @Override
    ClientRegistration clientRegistration(final OAuth2TokenExchangeGrantRequest grantRequest) {
        return grantRequest.getClientRegistration();
    }

    @Override
    Set<String> scopes(final OAuth2TokenExchangeGrantRequest grantRequest) {
        return grantRequest.getScopes();
    }

    @Override
    BodyInserters.FormInserter<String> populateTokenRequestBody(final OAuth2TokenExchangeGrantRequest grantRequest,
                                                                final BodyInserters.FormInserter<String> body) {
        final var subjectToken = grantRequest.getSubjectToken();
        final var subjectTokenType = grantRequest.getSubjectTokenType();
        final var audience = grantRequest.getAudience();
        return super.populateTokenRequestBody(grantRequest, body)
                .with(ExtendedOAuth2ParameterNames.SUBJECT_TOKEN, subjectToken.getTokenValue())
                .with(ExtendedOAuth2ParameterNames.SUBJECT_TOKEN_TYPE, subjectTokenType.value())
                .with(ExtendedOAuth2ParameterNames.AUDIENCE, audience);
    }
}
