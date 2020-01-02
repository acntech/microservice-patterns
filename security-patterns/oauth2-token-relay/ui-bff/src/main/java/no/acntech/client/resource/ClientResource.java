package no.acntech.client.resource;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.acntech.config.ClientProperties;

@RequestMapping(path = "client")
@RestController
public class ClientResource {

    private final ClientProperties clientProperties;

    public ClientResource(final ClientProperties clientProperties) {
        this.clientProperties = clientProperties;
    }

    @GetMapping(path = "config")
    public Map<String, Object> getConfig() {
        return clientProperties.getConfig();
    }
}
