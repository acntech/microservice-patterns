package no.acntech.security.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

@Repository
public class ApiKeyRepository {

    private UUID apiKey;

    public UUID getApiKey() {
        if (apiKey == null) {
            apiKey = UUID.randomUUID();
        }
        return apiKey;
    }
}
