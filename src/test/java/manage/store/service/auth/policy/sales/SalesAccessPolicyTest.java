package manage.store.service.auth.policy.sales;

import manage.store.config.auth.abac.policy.sales.SalesAccessPolicy;
import manage.store.consts.Tags;
import manage.store.model.common.branch.StoreBranch;
import manage.store.model.user.user.User;
import manage.store.model.user.value.UserAuthCode;
import manage.store.service.user.auth.UserAuthService;
import manage.store.service.user.auth.model.LoginUserDetails;
import manage.store.testUtils.common.StoreBranchTestUtils;
import manage.store.testUtils.user.UserTestUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@Tag(Tags.Test.UNIT)
@ExtendWith(MockitoExtension.class)
class SalesAccessPolicyTest {

    @Mock
    private UserAuthService userAuthService;

    @InjectMocks
    private SalesAccessPolicy salesAccessPolicy;

    @Test
    @DisplayName("canAccessStatistics - 성공_admin 권한")
    void canAccessStatistics_success_adminAuth() {
        // Given
        given(userAuthService.isUserAuthenticated()).willReturn(true);

        User user = UserTestUtils.DUMMY_USER1;
        LoginUserDetails loginUserDetails = new LoginUserDetails(user.getId(), user.getPassword(), user.getName(), UserAuthCode.ROLE_ADMIN, List.of());
        given(userAuthService.getLoginUserDetails()).willReturn(loginUserDetails);

        // When
        boolean canAccess = salesAccessPolicy.canAccessStatistics();

        // Then
        assertTrue(canAccess);
    }

    @Test
    @DisplayName("canAccessStatistics - 성공_owner 권한")
    void canAccessStatistics_success_ownerAuth() {
        // Given
        given(userAuthService.isUserAuthenticated()).willReturn(true);

        User user = UserTestUtils.DUMMY_USER2;
        StoreBranch branch = StoreBranchTestUtils.DUMMY_BRANCH2;
        LoginUserDetails loginUserDetails = new LoginUserDetails(user.getId(), user.getPassword(), user.getName(), UserAuthCode.ROLE_OWNER, List.of(branch));
        given(userAuthService.getLoginUserDetails()).willReturn(loginUserDetails);

        // When
        boolean canAccess = salesAccessPolicy.canAccessStatistics();

        // Then
        assertTrue(canAccess);
    }

    @Test
    @DisplayName("canAccessStatistics - 실패_비로그인")
    void cannotAccessStatistics_fail_notLogin() {
        // Given
        given(userAuthService.isUserAuthenticated()).willReturn(false);

        // When
        boolean canAccess = salesAccessPolicy.canAccessStatistics();

        // Then
        assertFalse(canAccess);
    }

    @Test
    @DisplayName("canAccessStatistics - 실패_권한없음(매니저)")
    void cannotAccessStatistics_fail_noAuth_manager() {
        // Given
        given(userAuthService.isUserAuthenticated()).willReturn(true);

        User user = UserTestUtils.DUMMY_USER3;
        StoreBranch branch = StoreBranchTestUtils.DUMMY_BRANCH1;
        LoginUserDetails loginUserDetails = new LoginUserDetails(user.getId(), user.getPassword(), user.getName(), UserAuthCode.ROLE_MANAGER, List.of(branch));
        given(userAuthService.getLoginUserDetails()).willReturn(loginUserDetails);

        // When
        boolean canAccess = salesAccessPolicy.canAccessStatistics();

        // Then
        assertFalse(canAccess);
    }

    @Test
    @DisplayName("canAccessStatistics - 실패_권한없음(아르바이트)")
    void cannotAccessStatistics_fail_noAuth_partTimer() {
        // Given
        given(userAuthService.isUserAuthenticated()).willReturn(true);

        User user = UserTestUtils.DUMMY_USER4;
        StoreBranch branch = StoreBranchTestUtils.DUMMY_BRANCH2;
        LoginUserDetails loginUserDetails = new LoginUserDetails(user.getId(), user.getPassword(), user.getName(), UserAuthCode.ROLE_PART_TIMER, List.of(branch));
        given(userAuthService.getLoginUserDetails()).willReturn(loginUserDetails);

        // When
        boolean canAccess = salesAccessPolicy.canAccessStatistics();

        // Then
        assertFalse(canAccess);
    }

}