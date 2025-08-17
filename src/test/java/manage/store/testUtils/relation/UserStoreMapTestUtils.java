package manage.store.testUtils.relation;

import manage.store.model.common.branch.StoreBranch;
import manage.store.model.user.user.User;
import manage.store.model.user.value.UserId;
import manage.store.testUtils.common.StoreBranchTestUtils;
import manage.store.testUtils.user.UserTestUtils;

import java.util.List;
import java.util.Map;

public interface UserStoreMapTestUtils {

    Map<UserId, List<StoreBranch>> storesByUserId = Map.of(
            UserTestUtils.DUMMY_USER1.getId(), List.of(StoreBranchTestUtils.DUMMY_BRANCH1, StoreBranchTestUtils.DUMMY_BRANCH2, StoreBranchTestUtils.DUMMY_BRANCH3),
            UserTestUtils.DUMMY_USER2.getId(), List.of(StoreBranchTestUtils.DUMMY_BRANCH2, StoreBranchTestUtils.DUMMY_BRANCH4),
            UserTestUtils.DUMMY_USER4.getId(), List.of(StoreBranchTestUtils.DUMMY_BRANCH1)
    );

    Map<String, List<User>> usersByBranchCd = Map.of(
            StoreBranchTestUtils.DUMMY_BRANCH1.getBranchCd(), List.of(UserTestUtils.DUMMY_USER1, UserTestUtils.DUMMY_USER4),
            StoreBranchTestUtils.DUMMY_BRANCH2.getBranchCd(), List.of(UserTestUtils.DUMMY_USER1, UserTestUtils.DUMMY_USER2),
            StoreBranchTestUtils.DUMMY_BRANCH3.getBranchCd(), List.of(UserTestUtils.DUMMY_USER1),
            StoreBranchTestUtils.DUMMY_BRANCH4.getBranchCd(), List.of(UserTestUtils.DUMMY_USER2)
    );

}