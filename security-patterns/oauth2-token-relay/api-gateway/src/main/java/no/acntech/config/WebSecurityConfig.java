package no.acntech.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;

@Configuration(proxyBeanMethods = false)
public class WebSecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(@NonNull final ServerHttpSecurity http,
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
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .logout().logoutSuccessHandler(new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository))
                .and()
                .build();
    }
}
