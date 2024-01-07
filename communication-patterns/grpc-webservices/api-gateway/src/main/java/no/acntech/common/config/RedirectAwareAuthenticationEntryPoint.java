package no.acntech.common.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
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

        return ServerWebExchangeMatchers.pathMatchers("/login*", "/error*")
                .matches(exchange)
                .flatMap(matchResult -> {
                    if (matchResult.isMatch()) {
                        return Mono.empty();
                    } else {
                        return authenticationRequiredResponse(exchange);
                    }
                });
    }

    private Mono<Void> authenticationRequiredResponse(final ServerWebExchange exchange) {
        try {
            final var response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            response.getHeaders().setLocation(URI.create("/login"));
            final var errorResponse = buildErrorResponse(exchange.getRequest());
            final var dataBuffer = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(errorResponse));
            return response.writeWith(Mono.just(dataBuffer));
        } catch (JsonProcessingException e) {
            throw new AuthenticationServiceException("Unable to write redirect response", e);
        }
    }

    private ProblemDetail buildErrorResponse(final ServerHttpRequest request) {
        final var problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
        problemDetail.setProperty("message", "Not authenticated");
        problemDetail.setProperty("path", request.getURI().getPath());
        return problemDetail;
    }
}
