package no.acntech.session.resource;

import no.acntech.session.model.SessionContext;
import no.acntech.session.service.SessionService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@RequestMapping(path = "/api/session")
@RestController
public class SessionResource {

    private final SessionService sessionService;

    public SessionResource(final SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public Mono<SessionContext> getSessionContext(final WebSession session,
                                                  final Authentication authentication) {
        return sessionService.getSession(session.getId(), authentication.getName());
    }
}
