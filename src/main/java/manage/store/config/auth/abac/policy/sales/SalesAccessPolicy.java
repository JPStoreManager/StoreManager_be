package manage.store.config.auth.abac.policy.sales;

import lombok.RequiredArgsConstructor;
import manage.store.config.auth.abac.policy.RolePolicy;
import manage.store.model.user.value.UserAuthCode;
import manage.store.service.user.auth.UserAuthService;
import manage.store.service.user.auth.model.LoginUserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 사용자가 가진 권한이 매출의 어떤 정보까지 확인할 수 있는지 검증하는 정책
 */
@Component
@RequiredArgsConstructor
public class SalesAccessPolicy implements RolePolicy {

    static final UserAuthCode[] ALLOWED_AUTH_CODES = {
        UserAuthCode.ROLE_ADMIN,
        UserAuthCode.ROLE_OWNER,
    };

    private final UserAuthService userAuthService;

    /**
     * 사용자가 가진 권한으로 매출의 통계 정보를 확인할 수 있는지 검증
     * @return true - 접근가능 | false - 접근불가
     */
    public boolean canAccessStatistics() {
        if(!userAuthService.isUserAuthenticated()) {
            return false;
        }

        LoginUserDetails user = userAuthService.getLoginUserDetails();
        UserAuthCode authCode = user.getAuthCode();

        return Arrays.asList(ALLOWED_AUTH_CODES).contains(authCode);
    }

}
