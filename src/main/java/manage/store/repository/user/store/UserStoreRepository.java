package manage.store.repository.user.store;

import manage.store.model.common.branch.StoreBranch;
import java.util.List;

public interface UserStoreRepository {


    /**
     * 사용자와 연관된 매장 목록 조회
     * @param userId 사용자 ID
     * @return List<StoreBranch> - 매장 목록
     */
    List<StoreBranch> selectStoreBranchesRelatedWithUser(String userId);


}
