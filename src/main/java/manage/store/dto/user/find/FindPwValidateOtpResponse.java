package manage.store.dto.user.find;

import manage.store.exception.common.InvalidParameterException;
import org.springframework.util.StringUtils;

public class FindPwValidateOtpResponse extends FindPwBaseResponse {

    private FindPwValidateOtpResponse(String sessionId) {
        super(sessionId);
    }

    /**
     * 비밀번호 찾기 OTP 검증 성공 응답 생성
     * @param sessionId 성공 시 발급되는 세션 아이디
     * @return FindPwValidateOtpResponse
     */
    public static FindPwValidateOtpResponse success(String sessionId) {
        if(!StringUtils.hasText(sessionId)) throw new InvalidParameterException("SessionId must not be null or empty on success. sessionId: " + sessionId);
        return new FindPwValidateOtpResponse(sessionId);
    }

    /**
     * 비밀번호 찾기 OTP 검증 실패 응답 생성
     * @return FindPwValidateOtpResponse
     */
    public static FindPwValidateOtpResponse fail() {
        return new FindPwValidateOtpResponse(null);
    }

}
