package manage.store.config.auth.login.response.fail;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import manage.store.config.WebConfiguration;
import manage.store.dto.common.BaseResponse;
import manage.store.consts.Message;
import manage.store.exception.common.auth.InvalidLoginUserDataException;
import manage.store.model.common.value.SuccessFlag;
import manage.store.utils.GsonUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

@Slf4j
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        Gson gson = GsonUtils.getGson();
        BaseResponse loginRes = new BaseResponse(SuccessFlag.N, Message.AUTH_ME_FAIL);

        if(isSevereError(exception)) {
            // TODO DB에 저장
            log.error("[중요 오류][데이터 오류 발생] 사용자의 데이터가 잘못되었습니다. Error Message: {}", exception.getMessage());
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401 인증 실패
        response.setContentType(WebConfiguration.RESPONSE_CONTENT_TYPE);
        response.getWriter().write(gson.toJson(loginRes));
    }

    /**
     * 로그인 과정에서 발생한 오류 중 데이터 이상 등으로 인한 로깅이 필요한지 판단
     */
    private boolean isSevereError(AuthenticationException e) {
        if(e != null &&
                e.getCause() != null &&
                e.getCause().getClass().equals(InvalidLoginUserDataException.class)) {
            return true;
        }

        return false;
    }
}
