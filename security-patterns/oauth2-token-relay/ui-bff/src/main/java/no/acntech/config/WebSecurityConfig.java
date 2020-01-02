package no.acntech.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableConfigurationProperties({
        SecurityProperties.class
})
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityProperties securityProperties;

    public WebSecurityConfig(final SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .loginPage(securityProperties.getLoginPage())
                .failureUrl(securityProperties.getFailureUrl())
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl(securityProperties.getLogoutSuccessUrl())
                .and()
                .oauth2Client();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .mvcMatchers(securityProperties.getWhitelistedPathsArray());
    }
}
