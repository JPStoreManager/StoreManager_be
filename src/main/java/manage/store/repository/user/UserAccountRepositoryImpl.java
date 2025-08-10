package manage.store.repository.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import manage.store.exception.common.InvalidParameterException;
import manage.store.model.user.user.User;
import manage.store.repository.user.mapper.UserAccountMapper;
import manage.store.utils.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserAccountRepositoryImpl implements UserAccountRepository {

    private final UserAccountMapper userAccountMapper;

    @Override
    public User selectUserById(String id) {
        if(!StringUtils.hasText(id)) throw new InvalidParameterException("User ID is empty");

        return userAccountMapper.selectUserById(id);
    }

    @Override
    public int insertUser(User user) {
        if(user == null) throw new InvalidParameterException("User is empty");

        try {
            return userAccountMapper.insertUser(user);
        } catch (DataIntegrityViolationException e) {
            log.info("Fail to insert user. User: {}, Error message: {}", user, ExceptionUtils.getExceptionErrorMsg(e));
            return 0;
        }
    }

    @Override
    public int updateUser(User user) {
        if(user == null) throw new InvalidParameterException("User is empty");

        try {
            return userAccountMapper.updateUser(user);
        } catch (DataIntegrityViolationException e) {
            log.info("Fail to update user. User: {}, Error message: {}", user, ExceptionUtils.getExceptionErrorMsg(e));
            return 0;
        }
    }
}
