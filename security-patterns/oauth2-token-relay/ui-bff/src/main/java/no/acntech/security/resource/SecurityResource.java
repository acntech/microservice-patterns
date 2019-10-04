package no.acntech.security.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import no.acntech.security.model.Session;
import no.acntech.security.service.SecurityService;

@RestController
public class SecurityResource {

    private final SecurityService securityService;

    public SecurityResource(final SecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping(path = "session/info")
    public ResponseEntity<Session> session() {
        Session session = securityService.getSession();
        return ResponseEntity.ok(session);
    }
}
