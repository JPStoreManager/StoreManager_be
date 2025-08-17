package manage.store.config.auth.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import manage.store.model.common.branch.StoreBranch;
import manage.store.model.user.user.User;
import manage.store.model.user.value.UserId;
import manage.store.repository.user.store.UserStoreRepository;
import manage.store.service.user.auth.UserAuthService;
import manage.store.service.user.auth.model.LoginUserDetails;
import manage.store.service.user.common.UserCommonService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 사용자 디테일 조회 서비스
 * LoginUserDetailsServiceImpl은 LoginAuthenticationProvider에서 조회되어 사용되는 로직
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginUserDetailsServiceImpl implements UserDetailsService {

    private final UserAuthService userAuthService;

    private final UserCommonService userCommonService;

    private final UserStoreRepository userStoreRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(!StringUtils.hasText(username)) throw new UsernameNotFoundException("Username is empty");

        User user = userCommonService.getUser(new UserId(username));
        if (!userAuthService.isUserActivated(user)) throw new UsernameNotFoundException("User not found with username: " + username);

        List<StoreBranch> userAccessibleBranch = userStoreRepository.selectStoreBranchesRelatedWithUser(user.getId().value());

        LoginUserDetails loginUserDetails = new LoginUserDetails(user.getId(), user.getPassword(), user.getName(), user.getAuthCd(), userAccessibleBranch);
        log.info("[loadUserByUsername] User found: {}", loginUserDetails);

        return loginUserDetails;
    }

}
