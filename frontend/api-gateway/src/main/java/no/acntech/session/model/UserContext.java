package no.acntech.session.model;

public record UserContext(
        String uid,
        String username,
        String firstName,
        String lastName,
        String[] roles) {
}
