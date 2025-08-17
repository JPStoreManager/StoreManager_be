package manage.store.repository.user.store;

import manage.store.exception.common.InvalidParameterException;
import manage.store.exception.common.db.DbOperDataAccessException;
import manage.store.exception.common.db.DbOperNonTransientException;
import manage.store.exception.common.db.DbOperOtherException;
import manage.store.exception.common.db.DbOperTransientException;
import manage.store.model.common.branch.StoreBranch;
import manage.store.repository.user.store.mapper.UserStoreMapper;
import manage.store.testUtils.common.StoreBranchTestUtils;
import manage.store.testUtils.user.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.jdbc.BadSqlGrammarException;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserStoreRepositoryImplTest {

    @Mock
    private UserStoreMapper userStoreMapper;

    @InjectMocks
    private UserStoreRepositoryImpl userStoreRepository;

    @Test
    @DisplayName("selectStoreBranchesRelatedWithUser 성공 - 사용자 연관 매장 목록 조회")
    void selectStoreBranchesRelatedWithUser_success() {
        // Given
        String userId = UserData.user1().getId().value();
        List<StoreBranch> expectedBranches = List.of(StoreBranchTestUtils.DUMMY_BRANCH1, StoreBranchTestUtils.DUMMY_BRANCH2);
        given(userStoreMapper.selectStoreBranchesRelatedWithUser(userId)).willReturn(expectedBranches);

        // When
        List<StoreBranch> actualBranches = userStoreRepository.selectStoreBranchesRelatedWithUser(userId);

        // Then
        assertThat(actualBranches).isEqualTo(expectedBranches);
    }

    @Test
    @DisplayName("selectStoreBranchesRelatedWithUser 성공 - 사용자 연관 매장 없음")
    void selectStoreBranchesRelatedWithUser_success_noResult() {
        // Given
        String userId = UserData.user1().getId().value();
        given(userStoreMapper.selectStoreBranchesRelatedWithUser(userId)).willReturn(Collections.emptyList());

        // When
        List<StoreBranch> actualBranches = userStoreRepository.selectStoreBranchesRelatedWithUser(userId);

        // Then
        assertThat(actualBranches).isNotNull();
        assertThat(actualBranches).isEmpty();
    }

    @Test
    @DisplayName("selectStoreBranchesRelatedWithUser 실패 - 잘못된 파라미터(userId가 null)")
    void selectStoreBranchesRelatedWithUser_fail_invalidParameter_null() {
        // Given
        String invalidUserId = null;

        // When & Then
        assertThrows(InvalidParameterException.class, () -> userStoreRepository.selectStoreBranchesRelatedWithUser(invalidUserId));
    }

    @Test
    @DisplayName("selectStoreBranchesRelatedWithUser 실패 - 잘못된 파라미터(userId가 empty)")
    void selectStoreBranchesRelatedWithUser_fail_invalidParameter_empty() {
        // Given
        String invalidUserId = "";

        // When & Then
        assertThrows(InvalidParameterException.class, () -> userStoreRepository.selectStoreBranchesRelatedWithUser(invalidUserId));
    }

    @Test
    @DisplayName("selectStoreBranchesRelatedWithUser 실패 - DB 조회 중 Non-transient 예외 발생")
    void selectStoreBranchesRelatedWithUser_fail_dbNonTransientException() {
        // Given
        String userId = UserData.user1().getId().value();
        when(userStoreMapper.selectStoreBranchesRelatedWithUser(userId))
                .thenThrow(new BadSqlGrammarException("test", "invalid sql", new SQLException()));

        // When & Then
        assertThrows(DbOperNonTransientException.class, () -> {
            userStoreRepository.selectStoreBranchesRelatedWithUser(userId);
        });
    }

    @Test
    @DisplayName("selectStoreBranchesRelatedWithUser 실패 - DB 조회 중 Transient 예외 발생")
    void selectStoreBranchesRelatedWithUser_fail_dbTransientException() {
        // Given
        String userId = UserData.user1().getId().value();
        when(userStoreMapper.selectStoreBranchesRelatedWithUser(userId))
                .thenThrow(new QueryTimeoutException("DB Error"));

        // When & Then
        assertThrows(DbOperTransientException.class, () -> {
            userStoreRepository.selectStoreBranchesRelatedWithUser(userId);
        });
    }

    @Test
    @DisplayName("selectStoreBranchesRelatedWithUser 실패 - DB 조회 중 일반 DataAccess 예외 발생")
    void selectStoreBranchesRelatedWithUser_fail_dbDataAccessException() {
        // Given
        String userId = UserData.user1().getId().value();
        // NonTransient/Transient 가 아닌 일반 DataAccessException 모의
        when(userStoreMapper.selectStoreBranchesRelatedWithUser(userId))
                .thenThrow(new DataAccessException("General DB Error"){});

        // When & Then
        assertThrows(DbOperDataAccessException.class, () -> {
            userStoreRepository.selectStoreBranchesRelatedWithUser(userId);
        });
    }

    @Test
    @DisplayName("selectStoreBranchesRelatedWithUser 실패 - DB 조회 중 기타 예외 발생")
    void selectStoreBranchesRelatedWithUser_fail_dbOtherException() {
        // Given
        String userId = UserData.user1().getId().value();
        when(userStoreMapper.selectStoreBranchesRelatedWithUser(userId))
                .thenThrow(new RuntimeException("Other Error"));

        // When & Then
        assertThrows(DbOperOtherException.class, () -> {
            userStoreRepository.selectStoreBranchesRelatedWithUser(userId);
        });
    }
}