package no.acntech.security.model;

public class Session {

    private String cookie;

    public String getCookie() {
        return cookie;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String cookie;

        private Builder() {
        }

        public Builder cookie(String cookie) {
            this.cookie = cookie;
            return this;
        }

        public Session build() {
            Session session = new Session();
            session.cookie = this.cookie;
            return session;
        }
    }
}
