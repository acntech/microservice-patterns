package no.acntech.model;

public record SessionContext(
        String sid,
        UserContext userContext
) {
}
