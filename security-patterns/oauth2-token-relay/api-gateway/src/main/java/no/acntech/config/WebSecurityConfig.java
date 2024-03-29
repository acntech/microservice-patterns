package no.acntech.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.acntech.cache.ReferrerAwareWebSessionRequestCache;
import no.acntech.error.ErrorResponseAuthenticationEntryPoint;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.savedrequest.ServerRequestCache;

@Configuration(proxyBeanMethods = false)
public class WebSecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(@NonNull final ServerHttpSecurity http,
                                                         @NonNull final ServerRequestCache serverRequestCache,
                                                         @NonNull final ServerAuthenticationEntryPoint authenticationEntryPoint,
                                                         @NonNull final ServerLogoutSuccessHandler serverLogoutSuccessHandler) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(config -> config
                        .pathMatchers("/", "/favicon.ico").permitAll()
                        .anyExchange().authenticated())
                .oauth2Login(Customizer.withDefaults())
                .requestCache(config -> config.requestCache(serverRequestCache))
                .exceptionHandling(config -> config.authenticationEntryPoint(authenticationEntryPoint))
                .logout(config -> config.logoutSuccessHandler(serverLogoutSuccessHandler))
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

    @Bean
    public ServerLogoutSuccessHandler serverLogoutSuccessHandler(final ReactiveClientRegistrationRepository clientRegistrationRepository) {
        return new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);
    }
}
