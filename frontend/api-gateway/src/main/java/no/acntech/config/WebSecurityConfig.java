package no.acntech.config;

import no.acntech.model.User;
import no.acntech.service.InMemoryReactiveUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.savedrequest.CookieServerRequestCache;

@Configuration(proxyBeanMethods = false)
public class WebSecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(@NonNull final ServerHttpSecurity http,
                                                         @NonNull final RedirectAwareAuthenticationEntryPoint authenticationEntryPoint) {
        return http
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/", "/favicon.ico", "/_api/**", "/webjars/**", "/login*").permitAll()
                .anyExchange().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .authenticationEntryPoint(authenticationEntryPoint)
                .authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler())
                .authenticationFailureHandler(new RedirectServerAuthenticationFailureHandler("/login?error"))
                .and()
                .requestCache().requestCache(new CookieServerRequestCache())
                .and()
                .build();
    }

    @Bean
    public InMemoryReactiveUserDetailsService reactiveUserDetailsService() {
        final var johnDoe = User.builder()
                .username("john.doe")
                .password("{noop}abcd1234")
                .firstName("John")
                .lastName("Doe")
                .roles("administrator")
                .build();
        final var janeDoe = User.builder()
                .username("jane.doe")
                .password("{noop}abcd1234")
                .firstName("Jane")
                .lastName("Doe")
                .roles("administrator")
                .build();
        final var jamesDoe = User.builder()
                .username("james.doe")
                .password("{noop}abcd1234")
                .firstName("James")
                .lastName("Doe")
                .roles("moderator")
                .build();
        final var jennyDoe = User.builder()
                .username("jenny.doe")
                .password("{noop}abcd1234")
                .firstName("Jenny")
                .lastName("Doe")
                .roles("moderator")
                .build();
        final var jimmyDoe = User.builder()
                .username("jimmy.doe")
                .password("{noop}abcd1234")
                .firstName("Jimmy")
                .lastName("Doe")
                .roles("user")
                .build();
        final var julieDoe = User.builder()
                .username("julie.doe")
                .password("{noop}abcd1234")
                .firstName("Julie")
                .lastName("Doe")
                .roles("user")
                .build();
        return new InMemoryReactiveUserDetailsService(johnDoe, janeDoe, jamesDoe, jennyDoe, jimmyDoe, julieDoe);
    }
}
