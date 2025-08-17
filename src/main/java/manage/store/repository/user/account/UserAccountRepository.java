package manage.store.repository.user.account;

import manage.store.model.user.user.User;
import manage.store.exception.common.InvalidParameterException;

public interface UserAccountRepository {

    /**
     * 사용자 ID로 사용자 정보 조회
     * @param id 사용자 ID
     * @return User - 사용자 정보 <br>
     *         null - 사용자 정보가 없을 경우
     */
    User selectUserById(String id);

    /**
     * 사용자 등록
     * @param user 사용자 정보
     * @return int - 등록된 사용자 수
     * @throws InvalidParameterException 사용자 validation에 실패한 경우
     */
    int insertUser(User user);

    /**
     * 사용자 정보 업데이트
     * @param user 사용자 정보
     * @return int - 등록된 사용자 수
     * @throws InvalidParameterException 사용자 validation에 실패한 경우
     */
    int updateUser(User user);

}
