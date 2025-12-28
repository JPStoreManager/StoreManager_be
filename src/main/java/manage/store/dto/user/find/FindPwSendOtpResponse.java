package manage.store.dto.user.find;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import manage.store.exception.common.InvalidParameterException;
import org.springframework.util.StringUtils;

public class FindPwSendOtpResponse extends FindPwBaseResponse {

    @JsonCreator
    private FindPwSendOtpResponse(@JsonProperty("sessionId") String sessionId) {
        super(sessionId);
    }

    /**
     * 비밀번호 찾기 OTP 전송 성공 응답 생성
     * @param sessionId 성공 시 발급되는 세션 아이디
     * @return FindPwSendOtpResponse
     * @throws InvalidParameterException sessionId가 비어있거나, 실패
     */
    public static FindPwSendOtpResponse success(String sessionId) {
        if(!StringUtils.hasText(sessionId)) throw new InvalidParameterException("SessionId must not be null or empty on success. sessionId: " + sessionId);
        return new FindPwSendOtpResponse(sessionId);
    }

    /**
     * 비밀번호 찾기 OTP 전송 실패 응답 생성
     * @return FindPwSendOtpResponse
     */
    public static FindPwSendOtpResponse fail() {
        return new FindPwSendOtpResponse(null);
    }

}
