package no.acntech.cache;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.web.server.savedrequest.ServerRequestCache;
import org.springframework.security.web.server.util.matcher.*;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;

public class ReferrerAwareWebSessionRequestCache implements ServerRequestCache {

    private static final String DEFAULT_SAVED_REQUEST_ATTR = "SPRING_SECURITY_SAVED_REQUEST";
    private static final ServerWebExchangeMatcher SAVED_REQUEST_MATCHER = createDefaultRequestMatcher();


    @Override
    public Mono<Void> saveRequest(final ServerWebExchange exchange) {
        return SAVED_REQUEST_MATCHER
                .matches(exchange)
                .filter(ServerWebExchangeMatcher.MatchResult::isMatch)
                .flatMap((m) -> exchange.getSession()).map(WebSession::getAttributes).doOnNext((attrs) -> {
                    final var requestPath = getRequestPath(exchange.getRequest());
                    attrs.put(DEFAULT_SAVED_REQUEST_ATTR, requestPath);
                }).then();
    }

    @Override
    public Mono<URI> getRedirectUri(final ServerWebExchange exchange) {
        return exchange.getSession()
                .flatMap((session) -> Mono.justOrEmpty(session.<String>getAttribute(DEFAULT_SAVED_REQUEST_ATTR)))
                .map(URI::create);
    }

    @Override
    public Mono<ServerHttpRequest> removeMatchingRequest(final ServerWebExchange exchange) {
        final var request = exchange.getRequest();
        return exchange.getSession()
                .map(WebSession::getAttributes)
                .filter((attributes) -> {
                    final var requestPath = getRequestPath(request);
                    return attributes.remove(DEFAULT_SAVED_REQUEST_ATTR, requestPath);
                }).map((attributes) -> request);
    }

    private static String getRequestPath(final ServerHttpRequest request) {
        final var referrer = request.getHeaders().get(HttpHeaders.REFERER);
        if (referrer == null || referrer.isEmpty()) {
            final var path = request.getPath().pathWithinApplication().value();
            final var query = request.getURI().getRawQuery();
            return path + ((query != null) ? "?" + query : "");
        } else {
            return referrer.get(0);
        }
    }

    private static ServerWebExchangeMatcher createDefaultRequestMatcher() {
        final var get = ServerWebExchangeMatchers.pathMatchers(HttpMethod.GET, "/**");
        final var notFavicon = new NegatedServerWebExchangeMatcher(ServerWebExchangeMatchers.pathMatchers("/favicon.*"));
        final var html = new MediaTypeServerWebExchangeMatcher(MediaType.TEXT_HTML);
        html.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
        return new AndServerWebExchangeMatcher(get, notFavicon, html);
    }
}
