package no.acntech.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.acntech.cache.ReferrerAwareWebSessionRequestCache;
import no.acntech.error.ErrorResponseAuthenticationEntryPoint;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.savedrequest.ServerRequestCache;

@Configuration(proxyBeanMethods = false)
public class WebSecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(@NonNull final ServerHttpSecurity http,
                                                         @NonNull final ServerRequestCache serverRequestCache,
                                                         @NonNull final ServerAuthenticationEntryPoint authenticationEntryPoint,
                                                         @NonNull final ReactiveClientRegistrationRepository clientRegistrationRepository) {
        return http
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/", "/favicon.ico").permitAll()
                .anyExchange().authenticated()
                .and()
                .oauth2Login()
                .and()
                .requestCache().requestCache(serverRequestCache)
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .logout().logoutSuccessHandler(new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository))
                .and()
                .build();
    }

    @Bean
    public ServerRequestCache serverRequestCache() {
        return new ReferrerAwareWebSessionRequestCache();
    }

    @Bean
    public ServerAuthenticationEntryPoint serverAuthenticationEntryPoint(final ObjectMapper objectMapper,
                                                                         final OAuth2ClientProperties properties) {
        return new ErrorResponseAuthenticationEntryPoint(objectMapper, properties);
    }
}
