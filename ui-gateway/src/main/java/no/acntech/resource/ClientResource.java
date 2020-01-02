package no.acntech.resource;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.acntech.config.ClientProperties;

@RequestMapping(path = "/api/client")
@RestController
public class ClientResource {

    private final ClientProperties clientProperties;

    public ClientResource(final ClientProperties clientProperties) {
        this.clientProperties = clientProperties;
    }

    @GetMapping(path = "config")
    public ResponseEntity<Object> getConfig() {
        Map<String, Object> config = clientProperties.getConfig();
        return ResponseEntity.ok(config);
    }

    @GetMapping(path = "config/{categoryName}")
    public ResponseEntity<Object> getConfigCategory(@PathVariable("categoryName") String categoryName) {
        Map<String, Object> config = clientProperties.getConfig();
        Object category = config.get(categoryName);
        if (category instanceof Map) {
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
            Map<String, Object> categoryMap = (Map<String, Object>) category;
            Object value = categoryMap.get(valueName);
            if (value instanceof Map) {
                return ResponseEntity.ok(value);
            }
        }
        return ResponseEntity.ok(Collections.emptyMap());
    }
}
