package manage.store.config.auth.response.fail;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import manage.store.config.WebConfiguration;
import manage.store.dto.common.BaseResponse;
import manage.store.consts.Message;
import manage.store.model.common.value.SuccessFlag;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

@Slf4j
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        Gson gson = new Gson();
        BaseResponse loginRes = new BaseResponse(SuccessFlag.N, Message.AUTH_ME_FAIL);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401 인증 실패
        response.setContentType(WebConfiguration.RESPONSE_CONTENT_TYPE);
        response.getWriter().write(gson.toJson(loginRes));
    }
}
