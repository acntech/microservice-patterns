package org.springframework.security.oauth2.core;

import org.springframework.security.oauth2.client.OAuth2AuthorizationContext;

public final class OAuth2AuthorizationContextAttribute {

    public static final String AUDIENCE_ATTRIBUTE_NAME = OAuth2AuthorizationContext.class.getName().concat(".AUDIENCE");
    public static final String SCOPES_ATTRIBUTE_NAME = OAuth2AuthorizationContext.class.getName().concat(".SCOPES");

    private OAuth2AuthorizationContextAttribute() {
    }
}
