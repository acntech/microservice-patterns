package no.acntech.model;

public record UserContext(
        String uid,
        String username,
        String firstName,
        String lastName,
        String[] roles) {
}
