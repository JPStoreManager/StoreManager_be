package manage.store.service.user.auth.model;

import manage.store.model.user.value.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class LoginUserDetails implements UserDetails{

    private final UserId userId;
    private final UserName name;
    private final UserAuthCode authCode;

    public LoginUserDetails(UserId userId, UserName name, UserAuthCode authCode) {
        if (userId == null || name == null || authCode == null) {
            throw new IllegalArgumentException("UserId, UserName, and UserAuthCode must not be null");
        }

        this.userId = userId;
        this.name = name;
        this.authCode = authCode;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(authCode.value()));
    }

    @Override
    public String getPassword() {
        throw new UnsupportedOperationException("Not supported for giving password");
    }

    @Override
    public String getUsername() {
        return userId.value();
    }

    public UserId getUserId() {
        return userId;
    }

    public UserName getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginUserDetails)) return false;

        LoginUserDetails that = (LoginUserDetails) o;

        if (!userId.equals(that.userId)) return false;
        if (!name.equals(that.name)) return false;
        return authCode.equals(that.authCode);
    }
}
