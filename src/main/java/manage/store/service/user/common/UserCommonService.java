package manage.store.service.user.common;


import manage.store.model.user.user.User;
import manage.store.model.user.value.UserId;

/**
 * User 테이블에 대한 단순 CRUD 기능 제공
 * 권한에 따라 validation 익셉션이 던져질 수 있음
 */
public interface UserCommonService {

    /**
     * 사용자 ID로 사용자 정보 조회
     * @param id 사용자 ID
     * @return User - 사용자 정보 <br>
     *         null - 사용자 정보가 없을 경우
     */
    User getUser(UserId id);

    /**
     * 사용자 등록
     * @param user 사용자 정보
     * @return User - 등록된 사용자 정보
     */
    User createUser(User user);

    /**
     * 사용자 정보 업데이트
     * @param user 사용자 정보
     * @return User - 업데이트된 사용자 정보
     */
    User updateUser(User user);

    /**
     * 사용자 삭제
     * @param userId 사용자 ID
     */
    void deleteUser(UserId userId);

}
