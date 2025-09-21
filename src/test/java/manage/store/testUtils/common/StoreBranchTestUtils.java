package manage.store.testUtils.common;

import manage.store.model.common.branch.StoreBranch;
import manage.store.model.common.value.SortOrder;
import manage.store.model.common.value.UseYn;
import manage.store.model.user.value.UserId;

public interface StoreBranchTestUtils {

    StoreBranch DUMMY_BRANCH1 = new StoreBranch(
            "DUMMY_BRCH1",
            "dummy branch1",
            "dummy branch1",
            "address1",
            UseYn.Y,
            new SortOrder(9999L),
            new UserId("system"),
            "2025-07-13 00:00:00.000",
            new UserId("system"),
            "2025-07-13 00:00:00.000"
    );

    StoreBranch DUMMY_BRANCH2 = new StoreBranch(
            "DUMMY_BRCH2",
            "dummy branch2",
            "dummy branch2",
            "address2",
            UseYn.Y,
            new SortOrder(9999L),
            new UserId("system"),
            "2025-07-13 00:00:00.000",
            new UserId("system"),
            "2025-07-13 00:00:00.000"
    );

    StoreBranch DUMMY_BRANCH3 = new StoreBranch(
            "DUMMY_BRCH3",
            "dummy branch3",
            "dummy branch3",
            "address3",
            UseYn.Y,
            new SortOrder(9999L),
            new UserId("system"),
            "2025-07-13 00:00:00.000",
            new UserId("system"),
            "2025-07-13 00:00:00.000"
    );

    StoreBranch DUMMY_BRANCH4 = new StoreBranch(
            "DUMMY_BRCH4",
            "dummy branch4",
            "dummy branch4",
            "address4",
            UseYn.N,
            new SortOrder(9999L),
            new UserId("system"),
            "2025-07-13 00:00:00.000",
            new UserId("system"),
            "2025-07-13 00:00:00.000"
    );

    StoreBranch[] USE_STORE_BRANCHES = {DUMMY_BRANCH1, DUMMY_BRANCH2, DUMMY_BRANCH3};
    StoreBranch[] NOT_USE_STORE_BRANCHES = {DUMMY_BRANCH4};

}