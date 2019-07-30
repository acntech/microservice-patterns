package no.acntech.security.resource;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginResource {

    private final ResourceLoader resourceLoader;

    public LoginResource(final ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping(path = "login/opaque", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> login(@RequestParam(name = "origin", required = false) String origin) throws IOException {
        if (StringUtils.hasText(origin)) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(origin));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } else {
            Resource resource = resourceLoader.getResource("classpath:/static/login.html");
            String page = new String(Files.readAllBytes(resource.getFile().toPath()));
            return ResponseEntity.ok(page);
        }
    }
}
