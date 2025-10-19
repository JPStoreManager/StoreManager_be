package manage.store.config.auth.login;

import lombok.RequiredArgsConstructor;
import manage.store.config.auth.login.user.LoginUserDetailsServiceImpl;
import manage.store.dto.common.BaseResult;
import manage.store.dto.user.login.LoginRequest;
import manage.store.service.user.login.LoginService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * 실질적인 인증 로직을 처리하는 클래스
 */
@Component
@RequiredArgsConstructor
public class LoginAuthenticationProvider implements AuthenticationProvider {

    private final LoginService loginService;

    private final LoginUserDetailsServiceImpl loginUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String id = authentication.getName();
        String password = authentication.getCredentials().toString();

        BaseResult loginResult = loginService.login(new LoginRequest(id, password));
        if(!loginResult.isSuccess()) throw new AuthenticationServiceException("Login failed. msg: " + loginResult.getMsg());

        UserDetails userDetails = loginUserDetailsService.loadUserByUsername(id);
        if (userDetails == null) throw new AuthenticationServiceException("User not found with username: " + id);

        // 3번째 파라미터에 값을 넣어주지 않으면 Spring Security가 인증 실패로 간주
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }


}
