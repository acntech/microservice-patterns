package no.acntech.converter;

import no.acntech.model.User;
import no.acntech.model.UserContext;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class UserToUserContextConverter implements Converter<User, UserContext> {

    @Nullable
    @Override
    public UserContext convert(@NonNull final User source) {
        final var roles = source.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.replace("ROLE_", ""))
                .toArray(String[]::new);
        return new UserContext(source.getUid(), source.getUsername(), source.getFirstName(), source.getLastName(), roles);
    }
}
