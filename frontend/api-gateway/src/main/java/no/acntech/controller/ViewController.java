package no.acntech.controller;

import no.acntech.model.UserContext;
import no.acntech.service.InMemoryReactiveUserDetailsService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class ViewController {

    private final InMemoryReactiveUserDetailsService reactiveUserDetailsService;

    public ViewController(final InMemoryReactiveUserDetailsService reactiveUserDetailsService) {
        this.reactiveUserDetailsService = reactiveUserDetailsService;
    }

    @GetMapping("/error")
    public Mono<Rendering> error() {
        return Mono.just(Rendering.view("error")
                .build());
    }

    @GetMapping("/login")
    public Mono<Rendering> login() {
        return Mono.just(Rendering.view("login-select-user")
                .modelAttribute("users", getUsers())
                .build());
    }

    private Flux<UserContext> getUsers() {
        return reactiveUserDetailsService.getUsers()
                .map(user -> new UserContext(
                        user.getUid(),
                        user.getUsername(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new)
                ));
    }
}
