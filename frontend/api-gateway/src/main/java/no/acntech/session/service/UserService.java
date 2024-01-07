package no.acntech.session.service;

import jakarta.validation.constraints.NotBlank;
import no.acntech.session.model.User;
import no.acntech.session.model.UserContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Validated
@Service
public class UserService {

    private final ConversionService conversionService;
    private final InMemoryReactiveUserDetailsService userDetailsService;

    public UserService(final ConversionService conversionService,
                       final InMemoryReactiveUserDetailsService userDetailsService) {
        this.conversionService = conversionService;
        this.userDetailsService = userDetailsService;
    }

    public Flux<UserContext> getUsers() {
        return userDetailsService.findAll()
                .mapNotNull(user -> conversionService.convert(user, UserContext.class));
    }

    public Mono<UserContext> getUser(@NotBlank final String username) {
        return userDetailsService.findByUsername(username)
                .filter(userDetails -> userDetails instanceof User)
                .map(userDetails -> (User) userDetails)
                .mapNotNull(user -> conversionService.convert(user, UserContext.class));
    }
}
