package manage.store.repository.user.account;

import manage.store.exception.common.InvalidParameterException;
import manage.store.exception.common.db.DbOperDataAccessException;
import manage.store.exception.common.db.DbOperNonTransientException;
import manage.store.exception.common.db.DbOperOtherException;
import manage.store.exception.common.db.DbOperTransientException;
import manage.store.model.user.user.User;
import manage.store.repository.user.account.mapper.UserAccountMapper;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAccountRepositoryImplTest {

    @Mock
    private UserAccountMapper userAccountMapper;

    @InjectMocks
    private UserAccountRepositoryImpl userAccountRepository;

    /**
     * selectUserById
     */
    @Test
    @DisplayName("selectUserById 성공 - 사용자 정보 조회 성공")
//    @Disabled("select에서는 별다른 validation이 없음으로 테스트 생략")
    void selectUserById_success() {
        // Given
        final User user = UserData.user1();
        given(userAccountMapper.selectUserById(user.getId().value())).willReturn(user);

        // When
        User selectedUser = userAccountRepository.selectUserById(user.getId().value());

        // Then
        assertThat(selectedUser).isNotNull();
        assertThat(selectedUser.getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("selectUserById 실패 - 잘못된 파라미터")
    void selectUserById_fail_invalidParameter() {
        // Given
        final String invalidUserId = null;

        // When - Then
        assertThrows(InvalidParameterException.class, () -> userAccountRepository.selectUserById(invalidUserId));
    }

    @Test
    @DisplayName("selectUserById 실패 - DB 조회 중 Non-transient 예외 발생")
    void selectUserById_fail_dbNonTransientException() {
        // Given
        String userId = UserData.user1().getId().value();
        when(userAccountMapper.selectUserById(userId))
                .thenThrow(new BadSqlGrammarException("test", "invalid sql", new SQLException()));

        // When & Then
        assertThrows(DbOperNonTransientException.class, () -> {
            userAccountRepository.selectUserById(userId);
        });
    }

    @Test
    @DisplayName("selectUserById 실패 - DB 조회 중 Transient 예외 발생")
    void selectUserById_fail_dbTransientException() {
        // Given
        String userId = UserData.user1().getId().value();
        when(userAccountMapper.selectUserById(userId))
                .thenThrow(new QueryTimeoutException("DB Error"));

        // When & Then
        assertThrows(DbOperTransientException.class, () -> {
            userAccountRepository.selectUserById(userId);
        });
    }

    @Test
    @DisplayName("selectUserById 실패 - DB 조회 중 일반 DataAccess 예외 발생")
    void selectUserById_fail_dbDataAccessException() {
        // Given
        String userId = UserData.user1().getId().value();
        // NonTransient/Transient 가 아닌 일반 DataAccessException 모의
        when(userAccountMapper.selectUserById(userId))
                .thenThrow(new DataAccessException("General DB Error"){});

        // When & Then
        assertThrows(DbOperDataAccessException.class, () -> {
            userAccountRepository.selectUserById(userId);
        });
    }

    @Test
    @DisplayName("selectUserById 실패 - DB 조회 중 기타 예외 발생")
    void selectUserById_fail_dbOtherException() {
        // Given
        String userId = UserData.user1().getId().value();
        when(userAccountMapper.selectUserById(userId))
                .thenThrow(new RuntimeException("Other Error"));

        // When & Then
        assertThrows(DbOperOtherException.class, () -> {
            userAccountRepository.selectUserById(userId);
        });
    }

    /**
     * insertUser
     */
    @Test
    @DisplayName("insertUser 성공 - 사용자 정보 등록 성공")
    void insertUser_success() {
        // Given
        final User user = UserData.user1();
        given(userAccountMapper.insertUser(user)).willReturn(1);

        // When
        int updatedCnt = userAccountRepository.insertUser(user);

        // Then
        assertThat(updatedCnt).isEqualTo(1);
    }

    @Test
    @DisplayName("insertUser 실패 - 잘못된 파라미터")
    void insertUser_fail_invalidParameter() {
        // Given
        final User user = UserData.user1();

        // When - Then
        assertThrows(InvalidParameterException.class, () -> userAccountRepository.insertUser(null));
    }

    @Test
    @DisplayName("insertUser 실패 - DB 조회 중 Non-transient 예외 발생")
    void insertUser_fail_dbNonTransientException() {
        // Given
        final User user = UserData.user1();
        when(userAccountMapper.insertUser(user))
                .thenThrow(new BadSqlGrammarException("test", "invalid sql", new SQLException()));

        // When & Then
        assertThrows(DbOperNonTransientException.class, () -> {
            userAccountRepository.insertUser(user);
        });
    }

    @Test
    @DisplayName("insertUser 실패 - DB 조회 중 Transient 예외 발생")
    void insertUser_fail_dbTransientException() {
        // Given
        final User user = UserData.user1();
        when(userAccountMapper.insertUser(user))
                .thenThrow(new QueryTimeoutException("DB Error"));

        // When & Then
        assertThrows(DbOperTransientException.class, () -> {
            userAccountRepository.insertUser(user);
        });
    }

    @Test
    @DisplayName("insertUser 실패 - DB 조회 중 일반 DataAccess 예외 발생")
    void insertUser_fail_dbDataAccessException() {
        // Given
        final User user = UserData.user1();
        // NonTransient/Transient 가 아닌 일반 DataAccessException 모의
        when(userAccountMapper.insertUser(user))
                .thenThrow(new DataAccessException("General DB Error") {});

        // When & Then
        assertThrows(DbOperDataAccessException.class, () -> {
            userAccountRepository.insertUser(user);
        });
    }

    @Test
    @DisplayName("insertUser 실패 - DB 조회 중 기타 예외 발생")
    void insertUser_fail_dbOtherException() {
        // Given
        final User user = UserData.user1();
        when(userAccountMapper.insertUser(user))
                .thenThrow(new RuntimeException("Other Error"));

        // When & Then
        assertThrows(DbOperOtherException.class, () -> {
            userAccountRepository.insertUser(user);
        });
    }

    /**
     * updateUser
     */
    @Test
    @DisplayName("updateUser 성공 - 사용자 정보 등록 성공")
    void updateUser_success() {
        // Given
        final User user = UserData.user1();
        given(userAccountMapper.updateUser(user)).willReturn(1);

        // When
        int updatedCnt = userAccountRepository.updateUser(user);

        // Then
        assertThat(updatedCnt).isEqualTo(1);
    }

    @Test
    @DisplayName("updateUser 실패 - 잘못된 파라미터")
    void updateUser_fail_invalidParameter() {
        // Given
        final User user = UserData.user1();

        // When - Then
        assertThrows(InvalidParameterException.class, () -> userAccountRepository.updateUser(null));
    }

    @Test
    @DisplayName("updateUser 실패 - DB 조회 중 Non-transient 예외 발생")
    void updateUser_fail_dbNonTransientException() {
        // Given
        final User user = UserData.user1();
        when(userAccountMapper.updateUser(user))
                .thenThrow(new BadSqlGrammarException("test", "invalid sql", new SQLException()));

        // When & Then
        assertThrows(DbOperNonTransientException.class, () -> {
            userAccountRepository.updateUser(user);
        });
    }

    @Test
    @DisplayName("updateUser 실패 - DB 조회 중 Transient 예외 발생")
    void updateUser_fail_dbTransientException() {
        // Given
        final User user = UserData.user1();
        when(userAccountMapper.updateUser(user))
                .thenThrow(new QueryTimeoutException("DB Error"));

        // When & Then
        assertThrows(DbOperTransientException.class, () -> {
            userAccountRepository.updateUser(user);
        });
    }

    @Test
    @DisplayName("updateUser 실패 - DB 조회 중 일반 DataAccess 예외 발생")
    void updateUser_fail_dbDataAccessException() {
        // Given
        final User user = UserData.user1();
        // NonTransient/Transient 가 아닌 일반 DataAccessException 모의
        when(userAccountMapper.updateUser(user))
                .thenThrow(new DataAccessException("General DB Error") {});

        // When & Then
        assertThrows(DbOperDataAccessException.class, () -> {
            userAccountRepository.updateUser(user);
        });
    }

    @Test
    @DisplayName("updateUser 실패 - DB 조회 중 기타 예외 발생")
    void updateUser_fail_dbOtherException() {
        // Given
        final User user = UserData.user1();
        when(userAccountMapper.updateUser(user))
                .thenThrow(new RuntimeException("Other Error"));

        // When & Then
        assertThrows(DbOperOtherException.class, () -> {
            userAccountRepository.updateUser(user);
        });
    }

}