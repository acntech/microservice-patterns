package no.acntech.service;

import no.acntech.model.User;
import org.springframework.security.core.userdetails.ReactiveUserDetailsPasswordService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryReactiveUserDetailsService implements ReactiveUserDetailsService, ReactiveUserDetailsPasswordService {

    private final Map<String, UserDetails> users;

    public InMemoryReactiveUserDetailsService(final UserDetails... users) {
        this(Arrays.asList(users));
    }

    public InMemoryReactiveUserDetailsService(final Collection<UserDetails> users) {
        Assert.notEmpty(users, "users cannot be null or empty");
        this.users = new ConcurrentHashMap<>();
        for (UserDetails user : users) {
            this.users.put(user.getUsername().toLowerCase(), user);
        }
    }

    @Override
    public Mono<UserDetails> findByUsername(final String username) {
        Assert.hasText(username, "username cannot be null or blank");
        return Mono.just(username)
                .map(String::toLowerCase)
                .map(this.users::get);
    }

    @Override
    public Mono<UserDetails> updatePassword(final UserDetails userDetails,
                                            final String newPassword) {
        Assert.notNull(userDetails, "user cannot be null");
        Assert.hasText(newPassword, "new password cannot be null or blank");
        return Mono.just(userDetails)
                .map(user -> this.users.get(user.getUsername()))
                .filter(user -> user instanceof User)
                .map(user -> (User) user)
                .map(user -> User.builder(user).password(newPassword).build())
                .mapNotNull(user -> this.users.put(user.getUsername().toLowerCase(), user));
    }

    public Flux<UserDetails> findAll() {
        return Flux.fromIterable(this.users.values());
    }
}
