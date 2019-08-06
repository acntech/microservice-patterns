package no.acntech.security.service;

import org.springframework.stereotype.Service;

import no.acntech.security.model.Session;

@Service
public class SecurityService {

    public Session getSession() {
        return Session.builder()
                .cookie("auth_token")
                .build();
    }
}
