package manage.store.service.auth.policy.branch;

import lombok.RequiredArgsConstructor;
import manage.store.model.common.branch.StoreBranch;
import manage.store.model.user.value.UserAuthCode;
import manage.store.service.auth.policy.RolePolicy;
import manage.store.service.user.auth.UserAuthService;
import manage.store.service.user.auth.model.LoginUserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 사용자가 지점에 접근할 수 있는지 확인하는 정책
 */
@Component
@RequiredArgsConstructor
public class BranchAccessPolicy implements RolePolicy {

    private final UserAuthService userAuthService;

    /**
     * 사용자가 특정 지점에 접근할 수 있는지 확인
     * 사용자가 로그인했을 때의 인증 정보와 지점 코드를 받아서 확인
     * @param branchCd 접근하려는 지점의 코드
     * @return true - 접근가능 | false - 접근불가
     */
    public boolean canAccess(String branchCd) {
        if(!userAuthService.isUserAuthenticated()) {
            return false;
        }

        LoginUserDetails user = userAuthService.getLoginUserDetails();

        UserAuthCode userAuthCode = user.getAuthCode();
        if(userAuthCode.equals(UserAuthCode.ROLE_ADMIN)) return true;
        else if(userAuthCode.equals(UserAuthCode.ROLE_OWNER) || userAuthCode.equals(UserAuthCode.ROLE_MANAGER) || userAuthCode.equals(UserAuthCode.ROLE_PART_TIMER)) {
            List<StoreBranch> accessibleBranches = user.getAccessibleBranches();
            return accessibleBranches.stream().anyMatch(branch -> branchCd.equals(branch.getBranchCd()));
        } else {
            return false;
        }
    }

}
