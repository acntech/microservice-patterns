package no.acntech.client.resource;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.acntech.config.ClientProperties;
import no.acntech.security.repository.ApiKeyRepository;

@RequestMapping(path = "/api/client")
@RestController
public class ClientResource {

    private static final String SECURITY_CATEGORY_NAME = "security";
    private static final String API_KEY_VALUE_NAME = "apiKey";
    private final ClientProperties clientProperties;
    private final ApiKeyRepository apiKeyRepository;

    public ClientResource(final ClientProperties clientProperties,
                          final ApiKeyRepository apiKeyRepository) {
        this.clientProperties = clientProperties;
        this.apiKeyRepository = apiKeyRepository;
    }

    @GetMapping(path = "config")
    public ResponseEntity<Object> getConfig() {
        Map<String, Object> config = clientProperties.getConfig();
        Object securityCategory = config.get(SECURITY_CATEGORY_NAME);
        Object updatedSecurityCategory = addApiKey(securityCategory);
        config.put(SECURITY_CATEGORY_NAME, updatedSecurityCategory);
        return ResponseEntity.ok(config);
    }

    @GetMapping(path = "config/{categoryName}")
    public ResponseEntity<Object> getConfigCategory(@PathVariable("categoryName") String categoryName) {
        Map<String, Object> config = clientProperties.getConfig();
        Object category = config.get(categoryName);
        if (category instanceof Map) {
            if (SECURITY_CATEGORY_NAME.equals(categoryName)) {
                Object updatedCategory = addApiKey(category);
                return ResponseEntity.ok(updatedCategory);
            }
            return ResponseEntity.ok(category);
        }
        return ResponseEntity.ok(Collections.emptyMap());
    }

    @SuppressWarnings("unchecked")
    @GetMapping(path = "config/{categoryName}/{valueName}")
    public ResponseEntity<Object> getConfigCategoryValue(@PathVariable("categoryName") String categoryName,
                                                         @PathVariable("valueName") String valueName) {
        Map<String, Object> config = clientProperties.getConfig();
        Object category = config.get(categoryName);
        if (category instanceof Map) {
            if (SECURITY_CATEGORY_NAME.equals(categoryName)) {
                Object updatedCategory = addApiKey(category);
            }
            Map<String, Object> categoryMap = (Map<String, Object>) category;
            Object value = categoryMap.get(valueName);
            if (value instanceof Map) {
                return ResponseEntity.ok(value);
            }
        }
        return ResponseEntity.ok(Collections.emptyMap());
    }

    @SuppressWarnings("unchecked")
    private Object addApiKey(Object category) {
        if (category instanceof Map) {
            Map<String, Object> categoryMap = (Map<String, Object>) category;
            categoryMap.put(API_KEY_VALUE_NAME, apiKeyRepository.getApiKey());
            return categoryMap;
        }
        return category;
    }
}
