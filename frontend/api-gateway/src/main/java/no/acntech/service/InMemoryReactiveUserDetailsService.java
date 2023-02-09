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

    private final Map<String, User> users;

    public InMemoryReactiveUserDetailsService(User... users) {
        this(Arrays.asList(users));
    }

    public InMemoryReactiveUserDetailsService(Collection<User> users) {
        Assert.notEmpty(users, "users cannot be null or empty");
        this.users = new ConcurrentHashMap<>();
        for (User user : users) {
            this.users.put(user.getUsername().toLowerCase(), user);
        }
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        Assert.hasText(username, "username cannot be null or blank");
        final var result = this.users.get(username.toLowerCase());
        return (result != null) ? Mono.just(org.springframework.security.core.userdetails.User.withUserDetails(result).build()) : Mono.empty();
    }

    @Override
    public Mono<UserDetails> updatePassword(UserDetails userDetails, String newPassword) {
        Assert.notNull(userDetails, "user cannot be null");
        Assert.hasText(newPassword, "new password cannot be null or blank");
        final var mono = Mono.just(userDetails)
                .map(user -> this.users.get(user.getUsername()))
                .map(user -> User.builder(user).password(newPassword).build())
                .doOnNext(user -> this.users.put(user.getUsername().toLowerCase(), user));
        return mono.map(UserDetails.class::cast);
    }

    public Flux<User> getUsers() {
        return Flux.fromIterable(this.users.values());
    }
}
