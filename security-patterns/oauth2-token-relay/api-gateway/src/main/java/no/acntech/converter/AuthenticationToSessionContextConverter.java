package no.acntech.converter;

import no.acntech.model.SessionContext;
import no.acntech.model.UserContext;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import org.springframework.web.server.WebSession;
import reactor.util.function.Tuple2;

@Component
public class AuthenticationToSessionContextConverter implements Converter<Tuple2<WebSession, Authentication>, SessionContext> {

    @Nullable
    @Override
    public SessionContext convert(@NonNull final Tuple2<WebSession, Authentication> source) {
        final var session = source.getT1();
        final var authentication = source.getT2();
        if (authentication.getPrincipal() instanceof OidcUser user) {
            final var uid = user.getIdToken().getClaimAsString("uid");
            final var roles = convertRoles(authentication);
            final var userContext = new UserContext(uid, user.getSubject(), user.getGivenName(), user.getFamilyName(), roles);
            return new SessionContext(session.getId(), userContext);
        }
        return null;
    }

    private String[] convertRoles(final Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.replace("ROLE_", ""))
                .toArray(String[]::new);
    }
}
