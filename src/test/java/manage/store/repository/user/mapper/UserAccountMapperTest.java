package manage.store.repository.user.mapper;

import manage.store.config.db.DBConfiguration;
import manage.store.consts.Profiles;
import manage.store.model.user.user.User;
import manage.store.testUtils.user.UserData;
import manage.store.testUtils.user.UserUtils;
import manage.store.testUtils.util.BaseDockerTest;
import org.junit.jupiter.api.*;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import manage.store.consts.Tags;

@Tag(Tags.Test.UNIT)
//@Testcontainers
@MybatisTest
@ActiveProfiles(Profiles.TEST)
@ContextConfiguration(classes = DBConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserAccountMapperTest extends BaseDockerTest {

    /** Docker container for Test */
//    @Container
//    private static final DockerComposeContainer composeContainer = getDockerComposeContainer();
//    static {
//        composeContainer.start();
//    }

    @Autowired
    private UserAccountMapper userAccountMapper;

    private static User[] users;

    @BeforeEach
    public void setUp() {
        final String idPrefix = "userId";

        users = new User[]{UserData.user1(), UserData.user2(), UserData.user3()};
        for (int i = 0; i < users.length; i++) {
            userAccountMapper.insertUser(users[i]);
        }
    }

    /** select */
    @Test
    @Order(1)
    @DisplayName("selectUserById 성공")
    void selectUserById_success() {
        // Given
        final User user = users[0];

        // When
        User dbUser = userAccountMapper.selectUserById(user.getId().value());

        // Then
        assertUser(user, dbUser);
    }

    @Test
    @Order(1)
    @DisplayName("selectUserById 실패 - user")
    void selectUserById_fail_noUser() {
        // Given
        final String noUserId = "noUserId";

        // When
        User dbUser = userAccountMapper.selectUserById(noUserId);

        // Then
        assertThat(dbUser).isNull();
    }


    /** insert */
    @Test
    @DisplayName("insertUser 성공")
    public void insertUser_success() {
        // Given
        final User user = UserData.user4();

        // When
        int result = userAccountMapper.insertUser(user);

        // Then
        assertEquals(1, result);
    }

    @Test
    @DisplayName("insertUser 실패 - 중복된 id(PK)")
    public void insertUser_fail_duplicateId_PK() {
        // Given
        final User user = UserData.user4();
        user.setId(users[0].getId());

        // When - Then
        assertThrows(DuplicateKeyException.class, () -> userAccountMapper.insertUser(user));
    }

    @Test
    @DisplayName("insertUser 실패 - 중복된 email(UNIQUE)")
    public void insertUser_fail_duplicateEmail_UNIQUE() {
        // Given
        final User user = UserData.user4();
        user.setEmail(users[0].getEmail());

        // When - Then
        assertThrows(DuplicateKeyException.class, () -> userAccountMapper.insertUser(user));
        assertThat(userAccountMapper.selectUserById(user.getId().value())).isNull();
    }

    @Test
    @DisplayName("insertUser 실패 - 중복된 phoneNo(UNIQUE)")
    public void insertUser_fail_duplicatePhoneNo_UNIQUE() {
        // Given
        final User user = UserData.user4();
        user.setPhoneNo(users[0].getPhoneNo());

        // When - Then
        assertThrows(DuplicateKeyException.class, () -> userAccountMapper.insertUser(user));
        assertThat(userAccountMapper.selectUserById(user.getId().value())).isNull();
    }

    @Test
    @DisplayName("insertUser 실패 - 중복된 residentRegistNo(UNIQUE)")
    public void insertUser_fail_duplicateResidentRegistNo_UNIQUE() {
        // Given
        final User user = UserData.user4();
        user.setResidentRegistNo(users[0].getResidentRegistNo());

        // When - Then
        assertThrows(DuplicateKeyException.class, () -> userAccountMapper.insertUser(user));
        assertThat(userAccountMapper.selectUserById(user.getId().value())).isNull();
    }

    /** update */
    @Test
    @DisplayName("updateUser 성공")
    public void updateUser_success() {
        // Given
        final User user = UserData.user4();
        user.setId(users[0].getId());

        // When
        int result = userAccountMapper.updateUser(user);

        // Then
        assertEquals(1, result);
        assertUser(user, userAccountMapper.selectUserById(user.getId().value()));
    }

    @Test
    @DisplayName("updateUser 실패 - 존재하지 않는 id")
    public void updateUser_fail_noUser() {
        // Given
        final User user = UserData.user4();

        // When
        int result = userAccountMapper.updateUser(user);

        // Then
        assertEquals(0, result);
    }

    @Test
    @DisplayName("updateUser 실패 - 중복된 email(UNIQUE)")
    public void updateUser_fail_duplicateEmail_UNIQUE() {
        // Given
        final User user = users[0];
        user.setEmail(users[1].getEmail());

        // When - Then
        assertThrows(DuplicateKeyException.class, () -> userAccountMapper.updateUser(user));
    }

    @Test
    @DisplayName("updateUser 실패 - 중복된 phoneNo(UNIQUE)")
    public void updateUser_fail_duplicatePhoneNo_UNIQUE() {
        // Given
        final User user = users[0];
        user.setPhoneNo(users[1].getPhoneNo());

        // When - Then
        assertThrows(DuplicateKeyException.class, () -> userAccountMapper.updateUser(user));
    }

    @Test
    @DisplayName("updateUser 실패 - 중복된 residentRegistNo(UNIQUE)")
    public void updateUser_fail_duplicateResidentRegistNo_UNIQUE() {
        // Given
        final User user = users[0];
        user.setResidentRegistNo(users[1].getResidentRegistNo());

        // When - Then
        assertThrows(DuplicateKeyException.class, () -> userAccountMapper.updateUser(user));
    }

    private void assertUser(User expected, User actual) {
        UserUtils.assertUser(expected, actual);
    }

}