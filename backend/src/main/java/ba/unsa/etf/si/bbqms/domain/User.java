package ba.unsa.etf.si.bbqms.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;

    @JsonIgnore
    private String password;
    private String phoneNumber;
    private boolean oauth;
    private String tfaSecret;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }


    public void setPassword(final String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isOauth() {
        return oauth;
    }

    public void setOauth(final boolean oauth) {
        this.oauth = oauth;
    }

    public String getTfaSecret() {
        return tfaSecret;
    }

    public void setTfaSecret(final String tfaSecret) {
        this.tfaSecret = tfaSecret;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(final Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toSet());
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        User user = new User();

        public UserBuilder id(final long id) {
            this.user.setId(id);
            return this;
        }

        public UserBuilder email(final String email) {
            this.user.setEmail(email);
            return this;
        }

        public UserBuilder password(final String password) {
            this.user.setPassword(password);
            return this;
        }

        public UserBuilder phoneNumber(final String phoneNumber) {
            this.user.setPhoneNumber(phoneNumber);
            return this;
        }

        public UserBuilder oAuth(final boolean oauth) {
            this.user.setOauth(oauth);
            return this;
        }

        public UserBuilder tfaSecret(final String tfaSecret) {
            this.user.setTfaSecret(tfaSecret);
            return this;
        }

        public UserBuilder roles(final Set<Role> roles) {
            this.user.setRoles(roles);
            return this;
        }

        public User build() {
            return this.user;
        }
    }
}
