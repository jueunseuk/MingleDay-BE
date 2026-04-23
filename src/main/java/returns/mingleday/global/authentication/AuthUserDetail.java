package returns.mingleday.global.authentication;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import returns.mingleday.domain.user.User;

import java.util.Collection;
import java.util.List;

@Getter
public class AuthUserDetail implements UserDetails {
    private final Integer userId;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUserDetail(User user) {
        this.userId = user.getUserId();
        this.password = user.getPassword();
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override public String getUsername() { return String.valueOf(userId); }
    @Override public String getPassword() { return password; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
}