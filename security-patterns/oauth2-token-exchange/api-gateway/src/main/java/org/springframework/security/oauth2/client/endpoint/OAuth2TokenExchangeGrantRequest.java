package org.springframework.security.oauth2.client.endpoint;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ExtendedAuthorizationTokenType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

import java.util.Set;

public class OAuth2TokenExchangeGrantRequest extends AbstractOAuth2AuthorizationGrantRequest {

    public static final AuthorizationGrantType GRANT_TYPE = new AuthorizationGrantType("urn:ietf:params:oauth:grant-type:token-exchange");

    private final OAuth2AccessToken subjectToken;
    private final ExtendedAuthorizationTokenType subjectTokenType;
    private final String audience;
    private final Set<String> scopes;

    public OAuth2TokenExchangeGrantRequest(final ClientRegistration clientRegistration,
                                           final OAuth2AccessToken subjectToken,
                                           final String audience,
                                           final Set<String> scopes) {
        super(GRANT_TYPE, clientRegistration);
        this.subjectToken = subjectToken;
        this.scopes = scopes;
        this.subjectTokenType = ExtendedAuthorizationTokenType.ACCESS_TOKEN;
        this.audience = audience;
    }

    public OAuth2AccessToken getSubjectToken() {
        return subjectToken;
    }

    public ExtendedAuthorizationTokenType getSubjectTokenType() {
        return subjectTokenType;
    }

    public String getAudience() {
        return audience;
    }

    public Set<String> getScopes() {
        return scopes;
    }
}
