package manage.store.repository.relation.user_store;

import manage.store.model.common.branch.StoreBranch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserStoreRelationMapper {

    List<StoreBranch> selectStoreBranchesRelatedWithUser(@Param("userId") String userId);

}
