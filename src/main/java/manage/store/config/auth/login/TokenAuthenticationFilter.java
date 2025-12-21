package manage.store.config.auth.login;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import manage.store.config.auth.login.user.LoginUserDetailsServiceImpl;
import manage.store.model.user.userAuth.LoginUserJwtClaim;
import manage.store.service.user.auth.JwtService;
import manage.store.service.user.auth.UserAuthService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 로그인 이후의 요청에 대해 JWT 토큰을 검증하고
 * SecurityContext에 인증 정보를 설정하는 필터
 */
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final String HEADER_AUTHORIZATION = "Authorization";

    private static final String TOKEN_PREFIX = "Bearer ";

    private final JwtService jwtService;

    private final LoginUserDetailsServiceImpl loginUserDetailsService;

    private final UserAuthService userAuthService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. 요청 헤더에서 JWT 토큰 추출
        String jwt = resolveToken(request);
        String userId = null;

        // 2. 토큰 유효성 검사
        try {
            LoginUserJwtClaim claim = jwtService.verify(jwt);
            if (claim != null) {
                userId = claim.userId();
            }
        } catch (Exception e) {
            // 토큰 파싱/검증 중 오류 발생 (예: 만료, 서명 불일치 등)
            logger.warn("JWT Token validation error: " + e.getMessage());
        }

        // 3. 아직 SecurityContext에 인증 정보가 없는 경우
        if (StringUtils.hasText(userId) && !userAuthService.isUserAuthenticated()) {
            UserDetails userDetails = loginUserDetailsService.loadUserByUsername(userId);

            // 5. AuthenticationToken 생성
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null, // JWT 방식에서는 비밀번호(Credentials)가 필요 없습니다.
                    userDetails.getAuthorities()
            );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 6. SecurityContext에 인증 정보 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 7. 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    /**
     * Request Header에서 "Bearer " 접두사를 제거하고 토큰 값만 추출합니다.
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER_AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }

        return null;
    }
}