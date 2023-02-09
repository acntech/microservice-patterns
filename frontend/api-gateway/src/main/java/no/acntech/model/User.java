package no.acntech.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.UUID;

public class User implements UserDetails {

    private final String uid;
    private final String username;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final Collection<GrantedAuthority> authorities;

    private User(String uid, String username, String password, String firstName, String lastName, Collection<GrantedAuthority> authorities) {
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.authorities = authorities;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(User user) {
        return new Builder(user.uid, user.username, user.password, user.firstName, user.lastName, user.authorities);
    }

    public static final class Builder {

        private final String uid;
        private String username;
        private String password;
        private String firstName;
        private String lastName;
        private final Collection<GrantedAuthority> authorities;

        private Builder() {
            this.uid = UUID.randomUUID().toString();
            this.authorities = new LinkedHashSet<>();
        }

        public Builder(String uid, String username, String password, String firstName, String lastName, Collection<GrantedAuthority> authorities) {
            this.uid = uid;
            this.username = username;
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
            this.authorities = authorities != null ? authorities : new LinkedHashSet<>();
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder roles(String... roles) {
            for (String role : roles) {
                this.authorities.add(new SimpleGrantedAuthority(role));
            }
            return this;
        }

        public User build() {
            return new User(uid, username, password, firstName, lastName, authorities);
        }
    }
}
