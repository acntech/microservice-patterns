package no.acntech.resource;

import no.acntech.model.SessionContext;
import no.acntech.model.UserContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
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
                    if (token.getPrincipal() instanceof OidcUser user) {
                        final var idToken = user.getIdToken();
                        final var userContext = new UserContext(idToken.getClaimAsString("uid"), user.getSubject(), user.getGivenName(), user.getFamilyName(), new String[]{}); // TODO: Fix roles
                        return new SessionContext(session.getId(), userContext);
                    }
                    return null;
                });
    }
}
