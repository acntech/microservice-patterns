package no.acntech.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableConfigurationProperties({
        SecurityProperties.class
})
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityProperties properties;

    public SecurityConfig(final SecurityProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers("")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .loginPage(properties.getLoginPage())
                .failureUrl(properties.getFailureUrl())
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl(properties.getLogoutSuccessUrl())
                .and()
                .oauth2Client();
    }
}
