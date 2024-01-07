package no.acntech.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
public class ErrorController {

    @GetMapping("/error")
    public Mono<Rendering> getError() {
        return Mono.just(Rendering.view("error")
                .build());
    }
}
