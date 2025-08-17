package manage.store.service.user.auth;

import manage.store.consts.Tags;
import manage.store.model.common.branch.StoreBranch;
import manage.store.model.common.value.DeleteFlag;
import manage.store.model.user.user.User;
import manage.store.service.user.auth.model.LoginUserDetails;
import manage.store.testUtils.common.StoreBranchTestUtils;
import manage.store.testUtils.user.UserData;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag(Tags.Test.UNIT)
@ExtendWith(MockitoExtension.class)
class UserAuthServiceImplTest {

    private static final String TAG_NOT_NEEDED_SETUP = "Tag_NotNeededSetup";

    @InjectMocks
    private UserAuthServiceImpl userAuthServiceImpl;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        if(!isTestNeedSetup(testInfo)) return;

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        if(!isTestNeedSetup(testInfo)) return;

        SecurityContextHolder.clearContext();
    }

    @Test
    @Tag(TAG_NOT_NEEDED_SETUP)
    @DisplayName("isUserActivated 성공 - 사용자 계정이 활성화 상태인 경우")
    void isUserActivated_success() {
        // given
        User user = UserData.user1();

        // when
        boolean result = userAuthServiceImpl.isUserActivated(user);

        // then
        assertTrue(result);
    }

    @Test
    @Tag(TAG_NOT_NEEDED_SETUP)
    @DisplayName("isUserActivated 실패 - 사용자 계정이 비활성화 상태")
    void isUserActivated_fail() {
        // given
        User user = UserData.user1();
        user.setDeleteFlag(DeleteFlag.YES);// 사용자 계정이 비활성화 상태로 설정

        // when
        boolean result = userAuthServiceImpl.isUserActivated(user);

        // then
        assertFalse(result);
    }

    @Test
    @Tag(TAG_NOT_NEEDED_SETUP)
    @DisplayName("isUserActivated 실패 - 사용자 계정이 null인 경우")
    void isUserActivated_fail_nullUser() {
        // given
        User user = null;

        // when
        boolean result = userAuthServiceImpl.isUserActivated(user);

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("getLoginUserDetails 성공 - 인증된 사용자 정보 반환")
    void getLoginUserDetails_success() {
        // given
        User user = UserData.user1();
        List<StoreBranch> userAccessibleBranches = List.of(StoreBranchTestUtils.DUMMY_BRANCH1, StoreBranchTestUtils.DUMMY_BRANCH2);
        LoginUserDetails loginUserDetails = new LoginUserDetails(user.getId(), user.getPassword(), user.getName(), user.getAuthCd(), userAccessibleBranches);
        when(authentication.getPrincipal()).thenReturn(loginUserDetails);

        // when
        UserDetails result = userAuthServiceImpl.getLoginUserDetails();

        // then
        assertNotNull(result);
        assertEquals(loginUserDetails, result);
    }

    @Test
    @DisplayName("getLoginUserDetails 실패 - Authentication 없음")
    void getLoginUserDetails_fail_NotExistAuthentication() {
        // given
        SecurityContextHolder.getContext().setAuthentication(null);

        // when - then
        assertThrows(AuthenticationServiceException.class, () -> userAuthServiceImpl.getLoginUserDetails());
    }

    @Test
    @DisplayName("getLoginUserDetails 실패 - 인증 정보가 null인 경우")
    void getLoginUserDetails_fail_nullAuthentication() {
        // given
        when(authentication.getPrincipal()).thenReturn(null);

        // when - then
        assertThrows(AuthenticationServiceException.class, () -> userAuthServiceImpl.getLoginUserDetails());
    }

    @Test
    @DisplayName("getLoginUserDetails 실패 - 인증 정보가 LoginUserDetails가 아닌 경우")
    void getLoginUserDetails_fail_invalidPrincipal() {
        // given
        when(authentication.getPrincipal()).thenReturn(mock(UserDetails.class));

        // when - then
        assertThrows(AuthenticationServiceException.class, () -> userAuthServiceImpl.getLoginUserDetails());
    }

    @Test
    @DisplayName("isUserAuthenticated 성공 - 인증된 사용자")
    void isUserAuthenticated_success() {
        // given
        User user = UserData.user1();
        List<StoreBranch> userAccessibleBranches = List.of(StoreBranchTestUtils.DUMMY_BRANCH1, StoreBranchTestUtils.DUMMY_BRANCH2);
        LoginUserDetails loginUserDetails = new LoginUserDetails(user.getId(), user.getPassword(), user.getName(), user.getAuthCd(), userAccessibleBranches);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(loginUserDetails);

        // when
        boolean result = userAuthServiceImpl.isUserAuthenticated();

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("isUserAuthenticated 실패 - 인증 정보가 없는 경우")
    void isUserAuthenticated_fail_nullAuthentication() {
        // given
        SecurityContextHolder.getContext().setAuthentication(null);

        // when
        boolean result = userAuthServiceImpl.isUserAuthenticated();

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("isUserAuthenticated 실패 - 인증되지 않은 사용자")
    void isUserAuthenticated_fail_notAuthenticated() {
        // given
        when(authentication.isAuthenticated()).thenReturn(false);

        // when
        boolean result = userAuthServiceImpl.isUserAuthenticated();

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("isUserAuthenticated 실패 - 인증된 사용자 정보가 없는 경우")
    void isUserAuthenticated_fail_noUserDetails() {
        // given
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(null);

        // when
        boolean result = userAuthServiceImpl.isUserAuthenticated();

        // then
        assertFalse(result);
    }

    private boolean isTestNeedSetup(TestInfo testInfo) {
        return !testInfo.getTags().contains(TAG_NOT_NEEDED_SETUP);
    }

}