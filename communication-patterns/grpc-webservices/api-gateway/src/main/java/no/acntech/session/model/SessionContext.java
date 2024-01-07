package no.acntech.session.model;

public record SessionContext(
        String sid,
        UserContext userContext
) {
}
