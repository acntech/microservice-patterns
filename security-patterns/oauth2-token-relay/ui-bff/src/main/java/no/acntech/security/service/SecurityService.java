package no.acntech.security.service;

import no.acntech.security.model.Session;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public Session getSession() {
        return Session.builder()
                .cookie("auth_token")
                .build();
    }
}
