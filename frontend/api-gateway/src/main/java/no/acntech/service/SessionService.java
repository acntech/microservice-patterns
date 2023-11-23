package no.acntech.service;

import jakarta.validation.constraints.NotBlank;
import no.acntech.model.SessionContext;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

@Validated
@Service
public class SessionService {

    private final UserService userService;

    public SessionService(final UserService userService) {
        this.userService = userService;
    }

    public Mono<SessionContext> getSession(@NotBlank final String sid,
                                           @NotBlank final String username) {
        return userService.getUser(username)
                .map(userContext -> new SessionContext(sid, userContext));
    }
}
