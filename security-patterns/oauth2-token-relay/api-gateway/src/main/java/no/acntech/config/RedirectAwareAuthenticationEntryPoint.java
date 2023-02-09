package no.acntech.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Component
public class RedirectAwareAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public RedirectAwareAuthenticationEntryPoint(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> commence(final ServerWebExchange exchange,
                               final AuthenticationException exception) {
        Assert.notNull(exchange, "exchange cannot be null");

        try {
            final var response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            final var redirectResponse = getRedirectResponse(exchange.getRequest());
            final var dataBuffer = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(redirectResponse));
            return response.writeWith(Mono.just(dataBuffer));
        } catch (JsonProcessingException e) {
            throw new AuthenticationServiceException("Unable to write redirect response", e);
        }
    }

    private ProblemDetail getRedirectResponse(final ServerHttpRequest request) {
        final var problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
        problemDetail.setProperty("message", "Not authenticated");
        problemDetail.setProperty("path", request.getURI().getPath());
        problemDetail.setProperty("redirectUrl", "/oauth2/authorization/acntech-token-relay-portal");
        return problemDetail;
    }
}
