package no.acntech.config;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "acntech.security")
@Valid
public class SecurityProperties {

    @NotBlank
    private String loginPage;
    @NotBlank
    private String failureUrl;
    @NotBlank
    private String logoutSuccessUrl;
    @NotNull
    private List<String> whitelistedPaths = new ArrayList<>();

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public String getFailureUrl() {
        return failureUrl;
    }

    public void setFailureUrl(String failureUrl) {
        this.failureUrl = failureUrl;
    }

    public String getLogoutSuccessUrl() {
        return logoutSuccessUrl;
    }

    public void setLogoutSuccessUrl(String logoutSuccessUrl) {
        this.logoutSuccessUrl = logoutSuccessUrl;
    }

    public List<String> getWhitelistedPaths() {
        return whitelistedPaths;
    }

    public void setWhitelistedPaths(List<String> whitelistedPaths) {
        this.whitelistedPaths = whitelistedPaths;
    }

    public String[] getWhitelistedPathsArray() {
        String[] whitelistedPathsArray = new String[whitelistedPaths.size()];
        return whitelistedPaths.toArray(whitelistedPathsArray);
    }
}
