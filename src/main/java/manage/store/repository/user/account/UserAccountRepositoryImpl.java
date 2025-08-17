package manage.store.repository.user.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import manage.store.exception.common.InvalidParameterException;
import manage.store.model.user.user.User;
import manage.store.repository.BaseRepository;
import manage.store.repository.user.account.mapper.UserAccountMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserAccountRepositoryImpl extends BaseRepository implements UserAccountRepository {

    private final UserAccountMapper userAccountMapper;

    @Override
    public User selectUserById(String id) {
        if(!StringUtils.hasText(id)) throw new InvalidParameterException("User ID is empty");

        try {
            return userAccountMapper.selectUserById(id);
        } catch (Exception e) {
            handleAndWrapException(e, "selectUserById", new Object[]{id});
            return null;
        }
    }

    @Override
    public int insertUser(User user) {
        if(user == null) throw new InvalidParameterException("User is empty");

        try {
            return userAccountMapper.insertUser(user);
        } catch (Exception e) {
            handleAndWrapException(e, "insertUser", new Object[]{user});
            return 0;
        }
    }

    @Override
    public int updateUser(User user) {
        if(user == null) throw new InvalidParameterException("User is empty");

        try {
            return userAccountMapper.updateUser(user);
        } catch (Exception e) {
            handleAndWrapException(e, "updateUser", new Object[]{user});
            return 0;
        }
    }
}
