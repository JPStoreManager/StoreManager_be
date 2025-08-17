package manage.store.repository.relation.user_store.mapper;

import manage.store.config.db.DBConfiguration;
import manage.store.consts.Profiles;
import manage.store.consts.Tags;
import manage.store.model.common.branch.StoreBranch;
import manage.store.model.common.value.UseYn;
import manage.store.model.user.value.UserId;
import manage.store.repository.relation.user_store.UserStoreRelationMapper;
import manage.store.testUtils.relation.UserStoreMapTestUtils;
import manage.store.testUtils.user.UserTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag(Tags.Test.UNIT)
@MybatisTest
@ActiveProfiles(Profiles.TEST)
@ContextConfiguration(classes = DBConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserStoreRelationMapperTest {

    @Autowired
    private UserStoreRelationMapper userStoreRelationMapper;

    @Test
    @DisplayName("selectStoreBranchesRelatedWithUser 성공")
    void selectStoreBranchesRelatedWithUser_success() {
        // Given
        final UserId userId = UserTestUtils.DUMMY_USER1.getId();

        // When
        List<StoreBranch> actual = userStoreRelationMapper.selectStoreBranchesRelatedWithUser(userId.value());

        // Then
        assertThat(actual).isNotNull();
        List<StoreBranch> expected = UserStoreMapTestUtils.storesByUserId.get(userId);
        assertThat(actual.size()).isEqualTo(expected.size());
        Collections.sort(actual, Comparator.comparing(StoreBranch::getBranchCd));
        for (int i = 0; i < actual.size(); i++) {
            assertStoreBranch(expected.get(i), actual.get(i));
        }
    }

    @Test
    @DisplayName("selectStoreBranchesRelatedWithUser 성공 - 연관된 지점 없음")
    void selectStoreBranchesRelatedWithUser_success_noRelations() {
        // Given
        final UserId userId = UserTestUtils.DUMMY_USER3.getId();

        // When
        List<StoreBranch> actual = userStoreRelationMapper.selectStoreBranchesRelatedWithUser(userId.value());

        // Then
        assertThat(actual).isNotNull();
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("selectStoreBranchesRelatedWithUser 실패 - 삭제된 사용자 아이디")
    void selectStoreBranchesRelatedWithUser_fail_deletedUser() {
        // Given
        final UserId userId = UserTestUtils.DUMMY_USER4.getId();

        // When
        List<StoreBranch> actual = userStoreRelationMapper.selectStoreBranchesRelatedWithUser(userId.value());

        // Then
        assertThat(actual).isNotNull();
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("selectStoreBranchesRelatedWithUser 실패 - 사용하지 않은 지점")
    void selectStoreBranchesRelatedWithUser_fail_notUsedBranch() {
        // Given
        final UserId userId = UserTestUtils.DUMMY_USER2.getId();

        // When
        List<StoreBranch> actual = userStoreRelationMapper.selectStoreBranchesRelatedWithUser(userId.value());

        // Then
        assertThat(actual).isNotNull();
        assertThat(actual).size().isEqualTo(UserStoreMapTestUtils.storesByUserId.get(userId).size() - 1);
    }

    @Test
    @DisplayName("selectStoreBranchesRelatedWithUser 실패 - 존재하지 않는 사용자")
    void selectStoreBranchesRelatedWithUser_fail_userNotFound() {
        // Given
        final String userId = "not-exist-user";

        // When
        List<StoreBranch> actual = userStoreRelationMapper.selectStoreBranchesRelatedWithUser(userId);

        // Then
        assertThat(actual).isNotNull();
        assertThat(actual).isEmpty();
    }

    void assertStoreBranch(StoreBranch expected, StoreBranch actual) {
        assertThat(expected).isNotNull();
        assertThat(actual).isNotNull();
        assertThat(expected.getBranchCd()).isEqualTo(actual.getBranchCd());
        assertThat(expected.getBranchNm()).isEqualTo(actual.getBranchNm());
        assertThat(expected.getBranchDesc()).isEqualTo(actual.getBranchDesc());
        assertThat(expected.getAddress()).isEqualTo(actual.getAddress());
        assertThat(expected.getUseYn()).isEqualTo(actual.getUseYn());
        assertThat(expected.getSortOrder()).isEqualTo(actual.getSortOrder());
    }

}