package manage.store.service.auth.policy.branch;

import manage.store.consts.Tags;
import manage.store.model.common.branch.StoreBranch;
import manage.store.model.user.user.User;
import manage.store.model.user.value.UserAuthCode;
import manage.store.service.user.auth.UserAuthService;
import manage.store.service.user.auth.model.LoginUserDetails;
import manage.store.testUtils.common.StoreBranchTestUtils;
import manage.store.testUtils.user.UserTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@Tag(Tags.Test.UNIT)
@ExtendWith(MockitoExtension.class)
class BranchAccessPolicyTest {

    @Mock
    private UserAuthService userAuthService;

    @InjectMocks
    private BranchAccessPolicy branchAccessPolicy;

    @Test
    @DisplayName("canAccess - 성공 admin 권한")
    void canAccess_success_adminAuthority() {
        // Given
        given(userAuthService.isUserAuthenticated()).willReturn(true);

        User user = UserTestUtils.DUMMY_USER1;
        List<StoreBranch> branches = List.of();
        final LoginUserDetails loginUserDetails = new LoginUserDetails(user.getId(), user.getPassword(), user.getName(), UserAuthCode.ROLE_ADMIN, branches);
        given(userAuthService.getLoginUserDetails()).willReturn(loginUserDetails);

        final String targetBranchCd = StoreBranchTestUtils.DUMMY_BRANCH1.getBranchCd();

        // When
        boolean canAccess = branchAccessPolicy.canAccess(targetBranchCd);

        // Then
        assertTrue(canAccess);
    }

    @Test
    @DisplayName("canAccess - 성공 owner/manager/partTimer 권한")
    void canAccess_success_ownerAuthority_accessibleBranch() {
        // <Owner>
        // Given
        given(userAuthService.isUserAuthenticated()).willReturn(true);

        User user = UserTestUtils.DUMMY_USER2;
        StoreBranch branch1 = StoreBranchTestUtils.DUMMY_BRANCH1;
        StoreBranch branch2 = StoreBranchTestUtils.DUMMY_BRANCH2;
        List<StoreBranch> branches = List.of(branch1, branch2);
        final LoginUserDetails loginUserDetails = new LoginUserDetails(user.getId(), user.getPassword(), user.getName(), UserAuthCode.ROLE_OWNER, branches);
        given(userAuthService.getLoginUserDetails()).willReturn(loginUserDetails);

        final String targetBranchCd1 = branch1.getBranchCd();
        final String targetBranchCd2 = branch2.getBranchCd();

        // When
        boolean canAccess1 = branchAccessPolicy.canAccess(targetBranchCd1);
        boolean canAccess2 = branchAccessPolicy.canAccess(targetBranchCd2);

        // Then
        assertTrue(canAccess1);
        assertTrue(canAccess2);


        // <Manager>
        // Given
        final LoginUserDetails loginUserDetails2 = new LoginUserDetails(user.getId(), user.getPassword(), user.getName(), UserAuthCode.ROLE_MANAGER, branches);
        given(userAuthService.getLoginUserDetails()).willReturn(loginUserDetails2);

        // When
        boolean canAccess3 = branchAccessPolicy.canAccess(targetBranchCd1);
        boolean canAccess4 = branchAccessPolicy.canAccess(targetBranchCd2);

        // Then
        assertTrue(canAccess3);
        assertTrue(canAccess4);


        // <PartTimer>
        // Given
        final LoginUserDetails loginUserDetails3 = new LoginUserDetails(user.getId(), user.getPassword(), user.getName(), UserAuthCode.ROLE_PART_TIMER, branches);
        given(userAuthService.getLoginUserDetails()).willReturn(loginUserDetails3);

        // When
        boolean canAccess5 = branchAccessPolicy.canAccess(targetBranchCd1);
        boolean canAccess6 = branchAccessPolicy.canAccess(targetBranchCd2);

        // Then
        assertTrue(canAccess5);
        assertTrue(canAccess6);
    }

    @Test
    @DisplayName("canAccess - 실패 비로그인")
    void canAccess_fail_notLogin() {
        // Given
        given(userAuthService.isUserAuthenticated()).willReturn(false);

        final String targetBranchCd = StoreBranchTestUtils.DUMMY_BRANCH1.getBranchCd();

        // When
        boolean canAccess = branchAccessPolicy.canAccess(targetBranchCd);

        // Then
        assertFalse(canAccess);
    }

    @Test
    @DisplayName("canAccess - 실패 owner/manager/partTimer 권한 접근 불가 지점")
    void canAccess_fail_ownerAuthority_inaccessibleBranch() {
        // <Owner>
        // Given
        given(userAuthService.isUserAuthenticated()).willReturn(true);
        User user = UserTestUtils.DUMMY_USER2;
        StoreBranch branch1 = StoreBranchTestUtils.DUMMY_BRANCH1;
        List<StoreBranch> branches = List.of(branch1);
        final LoginUserDetails loginUserDetails = new LoginUserDetails(user.getId(), user.getPassword(), user.getName(), UserAuthCode.ROLE_OWNER, branches);
        given(userAuthService.getLoginUserDetails()).willReturn(loginUserDetails);

        final String targetBranchCd = StoreBranchTestUtils.DUMMY_BRANCH2.getBranchCd();

        // When
        boolean canAccess = branchAccessPolicy.canAccess(targetBranchCd);

        // Then
        assertFalse(canAccess);

        // <Manager>
        // Given
        final LoginUserDetails loginUserDetails2 = new LoginUserDetails(user.getId(), user.getPassword(), user.getName(), UserAuthCode.ROLE_MANAGER, branches);
        given(userAuthService.getLoginUserDetails()).willReturn(loginUserDetails2);

        // When
        boolean canAccess2 = branchAccessPolicy.canAccess(targetBranchCd);

        // Then
        assertFalse(canAccess2);

        // <PartTimer>
        // Given
        final LoginUserDetails loginUserDetails3 = new LoginUserDetails(user.getId(), user.getPassword(), user.getName(), UserAuthCode.ROLE_PART_TIMER, branches);
        given(userAuthService.getLoginUserDetails()).willReturn(loginUserDetails3);

        // When
        boolean canAccess3 = branchAccessPolicy.canAccess(targetBranchCd);

        // Then
        assertFalse(canAccess3);
    }
}