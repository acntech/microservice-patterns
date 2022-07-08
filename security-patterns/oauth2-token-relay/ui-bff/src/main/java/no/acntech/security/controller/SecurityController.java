package no.acntech.security.controller;

import no.acntech.security.service.SecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SecurityController {

    private final SecurityService securityService;

    public SecurityController(final SecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping(path = "login/redirect")
    public ModelAndView login(@RequestParam(name = "origin", required = false) String origin) {
        if (StringUtils.hasText(origin)) {
            return new ModelAndView("redirect:" + origin);
        } else {
            return new ModelAndView("login");
        }
    }
}
