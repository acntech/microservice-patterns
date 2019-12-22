package no.acntech.config;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "acntech.gateway")
public class GatewayProperties {

    @NotBlank
    private String apiKeyHeader;
    @Valid
    @NotNull
    private RateLimiter rateLimiter = new RateLimiter();

    public String getApiKeyHeader() {
        return apiKeyHeader;
    }

    public void setApiKeyHeader(String apiKeyHeader) {
        this.apiKeyHeader = apiKeyHeader;
    }

    public RateLimiter getRateLimiter() {
        return rateLimiter;
    }

    public void setRateLimiter(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Validated
    public static class RateLimiter {

        @Valid
        @NotNull
        private RateLimiterProperties throttling = new RateLimiterProperties();

        public RateLimiterProperties getThrottling() {
            return throttling;
        }

        public void setThrottling(RateLimiterProperties throttling) {
            this.throttling = throttling;
        }
    }

    @Validated
    public static class RateLimiterProperties {

        @NotBlank
        private String remainingRequestsHeader;
        @NotBlank
        private String remainingRetryDelayMillisHeader;

        public String getRemainingRequestsHeader() {
            return remainingRequestsHeader;
        }

        public void setRemainingRequestsHeader(String remainingRequestsHeader) {
            this.remainingRequestsHeader = remainingRequestsHeader;
        }

        public String getRemainingRetryDelayMillisHeader() {
            return remainingRetryDelayMillisHeader;
        }

        public void setRemainingRetryDelayMillisHeader(String remainingRetryDelayMillisHeader) {
            this.remainingRetryDelayMillisHeader = remainingRetryDelayMillisHeader;
        }
    }
}
