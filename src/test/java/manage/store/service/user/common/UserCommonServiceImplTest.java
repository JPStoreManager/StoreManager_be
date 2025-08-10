package manage.store.service.user.common;

import manage.store.consts.Tags;
import manage.store.exception.common.DatabaseOperationException;
import manage.store.exception.common.InvalidParameterException;
import manage.store.model.common.value.DeleteFlag;
import manage.store.model.user.user.User;
import manage.store.model.user.value.UserId;
import manage.store.repository.user.UserAccountRepository;
import manage.store.testUtils.user.UserData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@Tag(Tags.Test.UNIT)
@ExtendWith(MockitoExtension.class)
class UserCommonServiceImplTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @InjectMocks
    private UserCommonServiceImpl userCommonService;

    @Test
    @DisplayName("getUser 성공")
    void getUser() {
        // Given
        final User user = UserData.user1();
        final UserId userId = user.getId();

        given(userAccountRepository.selectUserById(userId.value())).willReturn(user);

        // When
        User actual = userCommonService.getUser(userId);

        // Then
        Assertions.assertThat(actual).isEqualTo(user);
    }

    @Test
    @DisplayName("getUser 실패 - 사용자 ID가 null인 경우")
    void getUser_fail_NullUserId() {
        // Given
        UserId userId = null;

        // When & Then
        assertThrows(InvalidParameterException.class, () -> userCommonService.getUser(userId));
    }

    @Test
    @DisplayName("getUser 실패 - 사용자 정보가 없는 경우")
    void getUser_fail_NotExistUser() {
        // Given
        final UserId userId = UserData.user2().getId();

        given(userAccountRepository.selectUserById(userId.value()))
                .willReturn(null);

        // When
        User actual = userCommonService.getUser(userId);

        // Then
        Assertions.assertThat(actual).isNull();
    }

    @Test
    @DisplayName("createUser 성공")
    void createUser_success() {
        // Given
        final User user = UserData.user1();

        given(userAccountRepository.insertUser(user)).willReturn(1);

        // When
        User actual = userCommonService.createUser(user);

        // Then
        Assertions.assertThat(actual).isEqualTo(user);
    }

    @Test
    @DisplayName("createUser 실패 - 파라미터가 null인 경우")
    void createUser_fail_NullUser() {
        // Given
        User user = null;

        // When & Then
        assertThrows(InvalidParameterException.class, () -> userCommonService.createUser(user));
    }

    @Test
    @DisplayName("createUser 실패 - 등록된 사용자 수가 1이 아닌 경우(중복 데이터 / DB 에러 등)")
    void createUser_fail_NotInserted() {
        // Given
        final User user = UserData.user1();

        given(userAccountRepository.insertUser(user)).willReturn(0);

        // When & Then
        assertThrows(DatabaseOperationException.class, () -> userCommonService.createUser(user));
    }

    @Test
    @DisplayName("updateUser 성공")
    void updateUser_success() {
        // Given
        final User updateUser = UserData.user1();

        given(userAccountRepository.updateUser(updateUser)).willReturn(1);

        // When
        User actual = userCommonService.updateUser(updateUser);

        // Then
        Assertions.assertThat(actual).isEqualTo(updateUser);
    }

    @Test
    @DisplayName("updateUser 실패 - 파라미터가 null인 경우")
    void updateUser_fail_NullUser() {
        // Given
        final User user = null;

        // When & Then
        assertThrows(InvalidParameterException.class, () -> userCommonService.updateUser(user));
    }

    @Test
    @DisplayName("updateUser 실패 - 업데이트된 사용자 수가 1이 아닌 경우(중복 데이터 / DB 에러 등)")
    void updateUser_fail_NotUpdated() {
        // Given
        final User updateUser = UserData.user1();

        given(userAccountRepository.updateUser(updateUser)).willReturn(0);

        // When & Then
        assertThrows(DatabaseOperationException.class, () -> userCommonService.updateUser(updateUser));
    }

    @Test
    @DisplayName("deleteUser 성공")
    void deleteUser_success() {
        // Given
        final UserId userId = UserData.user1().getId();
        final User user = UserData.user1();
        user.setDeleteFlag(DeleteFlag.YES); // 삭제 플래그 설정

        given(userAccountRepository.selectUserById(userId.value())).willReturn(user);
        given(userAccountRepository.updateUser(user)).willReturn(1);

        // When
        userCommonService.deleteUser(userId);
    }

    @Test
    @DisplayName("deleteUser 실패 - 사용자 ID가 null인 경우")
    void deleteUser_fail_NullUserId() {
        // Given
        final UserId userId = null;

        // When & Then
        assertThrows(InvalidParameterException.class, () -> userCommonService.deleteUser(userId));
    }

    @Test
    @DisplayName("deleteUser 실패 - 사용자 정보가 없는 경우")
    void deleteUser_fail_NotExistUser() {
        // Given
        final UserId userId = UserData.user2().getId();

        given(userAccountRepository.selectUserById(userId.value())).willReturn(null);

        // When & Then
        assertThrows(InvalidParameterException.class, () -> userCommonService.deleteUser(userId));
    }

    @Test
    @DisplayName("deleteUser 실패 - 업데이트된 사용자 수가 1이 아닌 경우 (DB 에러 등)")
    void deleteUser_fail_NotUpdated() {
        // Given
        final UserId userId = UserData.user1().getId();
        final User user = UserData.user1();
        user.setDeleteFlag(DeleteFlag.YES); // 삭제 플래그 설정

        given(userAccountRepository.selectUserById(userId.value())).willReturn(user);
        given(userAccountRepository.updateUser(user)).willReturn(0);

        // When & Then
        assertThrows(DatabaseOperationException.class, () -> userCommonService.deleteUser(userId));
    }
}