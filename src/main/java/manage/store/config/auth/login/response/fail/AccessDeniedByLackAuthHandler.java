package manage.store.config.auth.login.response.fail;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manage.store.config.WebConfiguration;
import manage.store.dto.common.BaseResponse;
import manage.store.consts.Message;
import manage.store.model.common.value.SuccessFlag;
import manage.store.utils.GsonUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class AccessDeniedByLackAuthHandler implements AccessDeniedHandler {

    private final Gson gson = GsonUtils.getGson();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(WebConfiguration.RESPONSE_CONTENT_TYPE);
        String jsonResponse = gson.toJson(new BaseResponse(SuccessFlag.N, Message.AUTH_FAIL_LACK_AUTH));
        response.getWriter().write(jsonResponse);
    }

}
