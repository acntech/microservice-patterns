package no.acntech.limiter;

import java.time.Duration;

public class RateLimiterConfig {

    private int limit = 0;
    private Duration duration = Duration.ofSeconds(1);

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
