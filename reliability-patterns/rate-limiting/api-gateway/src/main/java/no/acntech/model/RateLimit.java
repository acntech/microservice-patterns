package no.acntech.model;

import java.io.Serializable;

public class RateLimit implements Serializable {

    private boolean allowed;
    private long remainingRequests;
    private long retryDelayMillis;

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public long getRemainingRequests() {
        return remainingRequests;
    }

    public void setRemainingRequests(long remainingRequests) {
        this.remainingRequests = remainingRequests;
    }

    public long getRetryDelayMillis() {
        return retryDelayMillis;
    }

    public void setRetryDelayMillis(long retryDelayMillis) {
        this.retryDelayMillis = retryDelayMillis;
    }
}
