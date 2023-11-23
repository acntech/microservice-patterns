package no.acntech.controller;

import no.acntech.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
public class ViewController {

    private final UserService userService;

    public ViewController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/error")
    public Mono<Rendering> error() {
        return Mono.just(Rendering.view("error")
                .build());
    }

    @GetMapping("/login")
    public Mono<Rendering> login() {
        return Mono.just(Rendering.view("login-select-user")
                .modelAttribute("users", userService.getUsers())
                .build());
    }
}
