package no.acntech.resource;

import no.acntech.model.SessionContext;
import no.acntech.model.User;
import no.acntech.model.UserContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@RequestMapping(path = "/api/session")
@RestController
public class SessionResource {

    @GetMapping
    public Mono<SessionContext> getSessionContext(final WebSession session,
                                                  final Authentication authentication) {
        return Mono.just(authentication)
                .mapNotNull(token -> {
                    if (token.getPrincipal() instanceof User user) {
                        final var roles = token.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
                        final var userContext = new UserContext(user.getUid(), user.getUsername(), user.getFirstName(), user.getLastName(), roles);
                        return new SessionContext(session.getId(), userContext);
                    }
                    return null;
                });
    }
}
