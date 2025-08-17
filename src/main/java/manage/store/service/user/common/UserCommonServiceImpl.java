package manage.store.service.user.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import manage.store.exception.common.db.DatabaseOperationException;
import manage.store.exception.common.InvalidParameterException;
import manage.store.model.common.value.DeleteFlag;
import manage.store.model.user.user.User;
import manage.store.model.user.value.UserId;
import manage.store.repository.user.account.UserAccountRepository;
import org.springframework.stereotype.Service;

/**
 * User 테이블에 대한 단순 CRUD 기능 제공
 * 권한에 따라 validation 익셉션이 던져질 수 있음
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserCommonServiceImpl implements UserCommonService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public User getUser(UserId id) {
        final String methodName = "getUser";
        log.debug("[{}] Param: {}", methodName, id);
        if (id == null) throw new InvalidParameterException("UserId is empty");

        User user = userAccountRepository.selectUserById(id.value());
        log.info("[{}] Result: {}", methodName, user);

        return user;
    }

    @Override
    public User createUser(User user) {
        final String methodName = "createUser";

        log.debug("[{}] Param: {}", methodName, user);
        if (user == null) throw new InvalidParameterException("User is empty");

        int insertedCnt = userAccountRepository.insertUser(user);
        if (insertedCnt != 1) throw new DatabaseOperationException("User insert failed. Updated count: " + insertedCnt);
        log.info("[{}] Result: {}", methodName, "success");

        return user;
    }

    @Override
    public User updateUser(User user) {
        final String methodName = "updateUser";

        log.debug("[{}] Param: {}", methodName, user);
        if (user == null) throw new InvalidParameterException("User is empty");

        int updatedCnt = userAccountRepository.updateUser(user);
        if (updatedCnt != 1) throw new DatabaseOperationException("User update failed. Updated count: " + updatedCnt);
        log.info("[{}] Result: {}", methodName, "success");

        return user;
    }

    @Override
    public void deleteUser(UserId userId) {
        final String methodName = "deleteUser";

        log.debug("[{}] Param: {}", methodName, userId);
        if(userId == null) throw new InvalidParameterException("userId is empty");

        User user = userAccountRepository.selectUserById(userId.value());
        if(user == null) throw new InvalidParameterException("User not found for id: " + userId);

        user.setDeleteFlag(DeleteFlag.YES);
        int updatedCnt = userAccountRepository.updateUser(user);
        if (updatedCnt != 1) {
            throw new DatabaseOperationException("User delete failed. Updated count: " + updatedCnt);
        }

        log.info("[{}] Result: {}", methodName, "success");
    }
}
