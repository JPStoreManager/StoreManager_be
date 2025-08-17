package manage.store.repository.user.store.mapper;

import manage.store.model.common.branch.StoreBranch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserStoreMapper {

    List<StoreBranch> selectStoreBranchesRelatedWithUser(@Param("userId") String userId);

}
