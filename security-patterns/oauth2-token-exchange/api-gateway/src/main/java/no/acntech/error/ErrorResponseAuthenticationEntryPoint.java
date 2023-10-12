package no.acntech.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.acntech.builder.ErrorResponseBuilder;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

public class ErrorResponseAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private final OAuth2ClientProperties properties;

    public ErrorResponseAuthenticationEntryPoint(final ObjectMapper objectMapper,
                                                 final OAuth2ClientProperties properties) {
        this.objectMapper = objectMapper;
        this.properties = properties;
    }

    @Override
    public Mono<Void> commence(final ServerWebExchange exchange,
                               final AuthenticationException exception) {
        Assert.notNull(exchange, "Exchange cannot be null");
        Assert.notNull(exception, "Exception cannot be null");

        final var registrationId = properties.getRegistration().keySet().stream()
                .findFirst()
                .orElseThrow(() -> new InternalAuthenticationServiceException("No registration ID found"));
        final var redirectUri = UriComponentsBuilder.fromPath("/oauth2/authorization")
                .pathSegment(registrationId)
                .build()
                .toUri();
        final var errorResponse = ErrorResponseBuilder.with(HttpStatus.UNAUTHORIZED)
                .instance(exchange.getRequest().getURI())
                .detail(exception.getMessage())
                .build();

        try {
            final var response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            response.getHeaders().setLocation(redirectUri);
            final var dataBuffer = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(errorResponse));
            return response.writeWith(Mono.just(dataBuffer));
        } catch (JsonProcessingException e) {
            throw new AuthenticationServiceException("Unable to write redirect response", e);
        }
    }
}
