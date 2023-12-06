package org.springframework.security.oauth2.core;

import org.springframework.util.Assert;

import java.io.Serializable;

public record ExtendedAuthorizationTokenType(String value) implements Serializable {

    public static final ExtendedAuthorizationTokenType ACCESS_TOKEN = new ExtendedAuthorizationTokenType(
            "urn:ietf:params:oauth:token-type:access_token");

    public ExtendedAuthorizationTokenType {
        Assert.hasText(value, "value cannot be empty");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        ExtendedAuthorizationTokenType that = (ExtendedAuthorizationTokenType) obj;
        return this.value().equals(that.value());
    }

    @Override
    public int hashCode() {
        return this.value().hashCode();
    }
}
