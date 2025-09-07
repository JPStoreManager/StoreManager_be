package manage.store.service.user.auth.model;

import lombok.Getter;
import lombok.ToString;
import manage.store.model.common.branch.StoreBranch;
import manage.store.model.user.value.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;

@Getter
@ToString
public class LoginUserDetails implements UserDetails{

    private final UserId userId;
    private final String password;
    private final UserName name;
    private final UserAuthCode authCode;
    private final List<StoreBranch> accessibleBranches;

    public LoginUserDetails(UserId userId, String password, UserName name, UserAuthCode authCode, List<StoreBranch> accessibleBranches) {
        if (userId == null || !StringUtils.hasText(password) || name == null || authCode == null || accessibleBranches == null) {
            throw new IllegalArgumentException("UserId, UserName, and UserAuthCode, AccessibleBranches must not be null");
        }

        if(!authCode.equals(UserAuthCode.ROLE_ADMIN) && accessibleBranches.isEmpty()) {
            throw new IllegalArgumentException("Accessible branches must not be empty for non-admin users");
        }

        this.userId = userId;
        this.password = password;
        this.name = name;
        this.authCode = authCode;
        this.accessibleBranches = accessibleBranches;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(authCode.value()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userId.value();
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
