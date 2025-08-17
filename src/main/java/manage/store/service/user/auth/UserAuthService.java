package manage.store.service.user.auth;

import manage.store.model.user.user.User;
import manage.store.service.user.auth.model.LoginUserDetails;
import org.springframework.security.authentication.AuthenticationServiceException;

public interface UserAuthService {

    /**
     * 사용자 계정이 삭제되지 않은 상태인지 확인
     * @param user 사용자 정보
     * @return boolean - 사용자 계정이 활성화 상태이면 true, 그렇지 않으면 false
     */
    boolean isUserActivated(User user);

    /**
     * 로그인한 사용자의 정보 조회
     * @return LoginUserDetails - 로그인한 사용자의 정보
     * @throws AuthenticationServiceException - 인증 정보가 없거나 잘못된 경우 발생
     */
    LoginUserDetails getLoginUserDetails();

    /**
     * 사용자 계정이 인증된 상태인지 확인
     * @return boolean - 사용자 계정이 인증된 상태이면 true, 그렇지 않으면 false
     */
    boolean isUserAuthenticated();

}
