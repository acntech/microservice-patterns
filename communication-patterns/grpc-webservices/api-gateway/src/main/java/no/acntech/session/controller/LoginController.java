package no.acntech.session.controller;

import no.acntech.session.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public Mono<Rendering> getLogin() {
        return Mono.just(Rendering.view("login-select-user")
                .modelAttribute("users", userService.getUsers())
                .build());
    }
}
