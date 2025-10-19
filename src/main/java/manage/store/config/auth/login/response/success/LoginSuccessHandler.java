package manage.store.config.auth.login.response.success;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import manage.store.config.WebConfiguration;
import manage.store.consts.Message;
import manage.store.dto.common.ApiResponse;
import manage.store.utils.GsonUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object credentials = authentication.getCredentials();

        Gson gson = GsonUtils.getGson();

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(WebConfiguration.RESPONSE_CONTENT_TYPE);
        response.getWriter().write(gson.toJson(ApiResponse.success(Message.LOGIN_SUCCESS)));

        clearAuthenticationAttributes(request);
    }

    protected final void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

}
