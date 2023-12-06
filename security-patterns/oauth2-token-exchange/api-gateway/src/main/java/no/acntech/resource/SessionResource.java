package no.acntech.resource;

import no.acntech.model.SessionContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

@RequestMapping(path = "/api/session")
@RestController
public class SessionResource {

    private final ConversionService conversionService;

    public SessionResource(final ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @GetMapping
    public Mono<SessionContext> getSessionContext(final WebSession session,
                                                  final Authentication authentication) {
        return Mono.just(authentication)
                .map(token -> Tuples.of(session, token))
                .mapNotNull(source -> conversionService.convert(source, SessionContext.class));
    }
}
