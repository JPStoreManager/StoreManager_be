package manage.store.repository.user.store;

import lombok.RequiredArgsConstructor;
import manage.store.exception.common.InvalidParameterException;
import manage.store.model.common.branch.StoreBranch;
import manage.store.repository.BaseRepository;
import manage.store.repository.user.store.mapper.UserStoreMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserStoreRepositoryImpl extends BaseRepository implements UserStoreRepository {

    private final UserStoreMapper userStoreMapper;

    @Override
    public List<StoreBranch> selectStoreBranchesRelatedWithUser(String userId) {
        if (!StringUtils.hasText(userId)) {
            throw new InvalidParameterException("User ID is empty");
        }

        try {
            return userStoreMapper.selectStoreBranchesRelatedWithUser(userId);
        } catch (Exception e) {
            handleAndWrapException(e, "selectStoreBranchesRelatedWithUser", new Object[]{userId});
            return null;
        }
    }
}