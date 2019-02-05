package no.acntech.common.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.security.oauth2.resource.JwtAccessTokenConverterConfigurer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This component is used to modify and enrich the creation of the OAuth2 authentication object so that it complies with Spring Security standards.
 * This is necessary because the KayCloak authorization server issues JWTs that does not have the format that Spring Security expects. Therefore information
 * such as roles are extracted explicitly by this component.
 */
@Component
public class JwtAccessTokenCustomizer extends DefaultAccessTokenConverter implements JwtAccessTokenConverterConfigurer {

    private static final String CLIENT_NAME_ELEMENT_IN_JWT = "resource_access";
    private static final String ROLE_ELEMENT_IN_JWT = "roles";

    private final ObjectMapper objectMapper;

    public JwtAccessTokenCustomizer(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Sets this component as the default access token converter.
     *
     * @param converter JWT access token converter helper component.
     */
    @Override
    public void configure(final JwtAccessTokenConverter converter) {
        converter.setAccessTokenConverter(this);
    }

    /**
     * Overrides method that is used to extract an OAuth2 authentication object from JWT in order to enrich the authentication object with additional information.
     *
     * @param tokenMap Map containing the contents of the JWT.
     * @return Enriched OAuth2 authentication object.
     */
    @Override
    public OAuth2Authentication extractAuthentication(final Map<String, ?> tokenMap) {
        final JsonNode token = objectMapper.convertValue(tokenMap, JsonNode.class);

        final List<GrantedAuthority> authorities = extractRoles(token);

        final OAuth2Authentication oAuth2Authentication = super.extractAuthentication(tokenMap);
        final OAuth2Request oAuth2Request = createOAuth2Request(oAuth2Authentication.getOAuth2Request(), authorities);

        Authentication authentication = createAuthentication(oAuth2Authentication, authorities);
        return new OAuth2Authentication(oAuth2Request, authentication);
    }

    /**
     * Extract roles from JWT and creating list of granted authorities.
     *
     * @param token JWT as a JSON node.
     * @return List of granted authorities.
     */
    private List<GrantedAuthority> extractRoles(final JsonNode token) {
        List<String> roles = new ArrayList<>();
        token.path(CLIENT_NAME_ELEMENT_IN_JWT)
                .elements()
                .forEachRemaining(e -> e.path(ROLE_ELEMENT_IN_JWT)
                        .elements()
                        .forEachRemaining(r -> roles.add("ROLE_" + r.asText())));
        return AuthorityUtils.createAuthorityList(roles.toArray(new String[0]));
    }

    /**
     * Create new OAuth2 request object by copying all fields from existing object and adding granted authorities.
     *
     * @param oAuth2Request Existing OAuth2 request object.
     * @param authorities   Extracted granted authorities.
     * @return New OAuth2 request object.
     */
    private OAuth2Request createOAuth2Request(final OAuth2Request oAuth2Request, final List<GrantedAuthority> authorities) {
        return new OAuth2Request(
                oAuth2Request.getRequestParameters(),
                oAuth2Request.getClientId(),
                authorities,
                oAuth2Request.isApproved(),
                oAuth2Request.getScope(),
                oAuth2Request.getResourceIds(),
                oAuth2Request.getRedirectUri(),
                oAuth2Request.getResponseTypes(),
                oAuth2Request.getExtensions()
        );
    }

    /**
     * Create new authentication token with granted authorities.
     *
     * @param oAuth2Authentication Existing OAuth2 authentication object.
     * @param authorities          Extracted granted authorities.
     * @return New authentication token.
     */
    private Authentication createAuthentication(final OAuth2Authentication oAuth2Authentication, final List<GrantedAuthority> authorities) {
        return new UsernamePasswordAuthenticationToken(oAuth2Authentication.getPrincipal(), "N/A", authorities);
    }
}
