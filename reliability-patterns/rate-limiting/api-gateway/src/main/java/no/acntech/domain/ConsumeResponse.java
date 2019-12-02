package no.acntech.domain;

public class ConsumeResponse {

    private boolean allowed;
    private long remainingRequests;
    private long retryDelayMs;

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

    public long getRetryDelayMs() {
        return retryDelayMs;
    }

    public void setRetryDelayMs(long retryDelayMs) {
        this.retryDelayMs = retryDelayMs;
    }
}
