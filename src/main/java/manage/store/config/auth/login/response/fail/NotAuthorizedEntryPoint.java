package manage.store.config.auth.login.response.fail;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manage.store.config.WebConfiguration;
import manage.store.dto.common.ApiResponse;
import manage.store.consts.Message;
import manage.store.utils.GsonUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class NotAuthorizedEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Gson gson = GsonUtils.getGson();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(WebConfiguration.RESPONSE_CONTENT_TYPE);
        response.getWriter().write(gson.toJson(ApiResponse.fail(Message.AUTH_FAIL_NOT_AUTHENTICATED)));
    }

}
