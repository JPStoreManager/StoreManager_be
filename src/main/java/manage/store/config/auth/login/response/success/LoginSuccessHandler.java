package manage.store.config.auth.login.response.success;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import manage.store.config.WebConfiguration;
import manage.store.consts.Message;
import manage.store.dto.auth.LoginResponse;
import manage.store.dto.common.ApiResponse;
import manage.store.service.user.auth.JwtService;
import manage.store.service.user.auth.model.LoginUserDetails;
import manage.store.utils.ObjectMapperUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object credentials = authentication.getCredentials();
        LoginUserDetails principal = (LoginUserDetails) authentication.getPrincipal();

        ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();

        String jwt = jwtService.create(principal.getUserId());
        LoginResponse responseData = new LoginResponse(principal.getUserId(), jwt);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(WebConfiguration.RESPONSE_CONTENT_TYPE);
        response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.success(responseData, Message.LOGIN_SUCCESS)));

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
