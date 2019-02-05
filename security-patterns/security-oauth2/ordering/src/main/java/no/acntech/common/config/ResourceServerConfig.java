package no.acntech.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

/**
 * Configuration class that enables OAuth2 resource server.
 */
@EnableResourceServer
@Configuration
public class ResourceServerConfig {

    @Primary
    @Bean
    public RemoteTokenServices tokenService() {
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl("http://localhost:8000/oauth/check_token");
        tokenService.setClientId("ordering");
        tokenService.setClientSecret("secret");
        return tokenService;
    }
}
