package no.acntech.resource;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/api/client")
@RestController
public class ClientResource {

    @GetMapping(path = "config")
    public Map<String, Object> getConfig() {
        return new LinkedHashMap<>();
    }
}
