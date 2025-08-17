package manage.store.repository.user.account.mapper;

import manage.store.model.user.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserAccountMapper {

    User selectUserById(@Param(value = "id") String id);

    int insertUser(User user);

    int updateUser(User user);

}
