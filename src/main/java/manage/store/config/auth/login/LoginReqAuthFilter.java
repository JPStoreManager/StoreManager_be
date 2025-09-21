package manage.store.config.auth.login;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import manage.store.dto.user.login.LoginRequest;
import manage.store.utils.ApiPathUtils;
import manage.store.utils.GsonUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 인증 처리 entryPoint 설정이자 인증 로직 처리 전 요청 검증 필터 클래스
 */
@Slf4j
public class LoginReqAuthFilter extends AbstractAuthenticationProcessingFilter {

    private static final String LOGIN_API_PATH = ApiPathUtils.getPath(ApiPathUtils.ApiName.LOGIN);

    private static final String LOGIN_API_HTTP_METHOD = HttpMethod.POST.name();

    private static final String LOGIN_API_CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;

    private final Gson gson;

    public LoginReqAuthFilter(HttpSecurity httpSecurity) {
        super(new AntPathRequestMatcher(LOGIN_API_PATH, LOGIN_API_HTTP_METHOD));
        this.gson = GsonUtils.getGson();

        // 인증 성공 시 SecurityContext를 저장할 SecurityContextRepository 타입 설정
        setSecurityContextRepository(getSecurityContextRepository(httpSecurity));
    }

    /**
     * SecurityContextRepository 조회 메서드
     * @param http SecurityCongiguration에서 설정중인 HttpSecurity 객체를 이어서 활용하기 위한 파라미터
     */
    private SecurityContextRepository getSecurityContextRepository(HttpSecurity http) {
        SecurityContextRepository securityContextRepository = http.getSharedObject(SecurityContextRepository.class);
        if (securityContextRepository == null) {
            securityContextRepository = new DelegatingSecurityContextRepository(
                    new RequestAttributeSecurityContextRepository(), new HttpSessionSecurityContextRepository());
        }
        return securityContextRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if(!isRequestHeaderValid(request)) {
            log.info("Invalid Login Request Entered. Method: {}, Content-Type: {}", request.getMethod(), request.getContentType());
            throw new AuthenticationServiceException("Not Valid Login Request");
        }

        UsernamePasswordAuthenticationToken authToken = null;
        try {
            String requestBodyStr = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            LoginRequest loginRequest = gson.fromJson(requestBodyStr, LoginRequest.class);
            /** 1차 검증 */
            if (!StringUtils.hasText(loginRequest.getId()) || !StringUtils.hasText(loginRequest.getPassword())) {
                log.info("Invalid Login Request Entered. Body: {}", loginRequest);
                throw new AuthenticationServiceException("Not Valid Login Request");
            }

            authToken = new UsernamePasswordAuthenticationToken(loginRequest.getId(), loginRequest.getPassword());
        } catch (IOException | JsonSyntaxException e) {
            throw new AuthenticationServiceException("Not Valid Login Request");
        }

        return this.getAuthenticationManager().authenticate(authToken);
    }

    private boolean isRequestHeaderValid(HttpServletRequest request) {
        return LOGIN_API_HTTP_METHOD.equals(request.getMethod()) && LOGIN_API_CONTENT_TYPE.equals(request.getContentType());
    }
}
