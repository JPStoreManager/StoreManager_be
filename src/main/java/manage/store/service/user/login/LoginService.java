package manage.store.service.user.login;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import manage.store.dto.common.BaseResult;
import manage.store.dto.user.login.LoginRequest;
import manage.store.exception.common.InvalidParameterException;

public interface LoginService {

    /**
     * 사용자의 아이디와 비밀번호를 받아 로그인을 시도한다.
     * @param loginDTO 사용자의 로그인 정보를 담음 객체(id, password 필수)
     * @throws ConstraintViolationException id 혹은 password가 빈 값일 경우
     * @return LoginResponse 로그인 결과 객체 <br>
     *  result {@code SuccessFlag}: 로그인 성공 여부 (Y: 성공, N: 실패)
     *  msg {@code string} (optional): 실패 시 에러 메시지
     */
    BaseResult login(@Valid LoginRequest loginDTO);

}
