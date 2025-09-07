package manage.store.config.auth;

import lombok.RequiredArgsConstructor;
import manage.store.config.auth.login.LoginReqAuthFilter;
import manage.store.config.auth.login.response.fail.AccessDeniedByLackAuthHandler;
import manage.store.config.auth.login.response.fail.LoginFailureHandler;
import manage.store.config.auth.login.response.fail.NotAuthorizedEntryPoint;
import manage.store.config.auth.login.response.success.LoginSuccessHandler;
import manage.store.config.auth.login.user.LoginUserDetailsServiceImpl;
import manage.store.utils.ApiPathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Value("${cors.allow-ip}")
    private String[] allowedIps;

    private final AuthenticationProvider loginAuthenticationProvider;

    private final LoginUserDetailsServiceImpl loginUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }

    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new NotAuthorizedEntryPoint();
    }

    @Bean
    @Autowired
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        final String[] NOT_NEED_AUTH_APIS = {
                ApiPathUtils.ApiPath.User.LOGIN,
                ApiPathUtils.ApiPath.User.FindPassword.SEND_OTP,
                ApiPathUtils.ApiPath.User.FindPassword.VALIDATE_OTP,
                ApiPathUtils.ApiPath.User.FindPassword.UPDATE_PW,
        };

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(loginAuthenticationProvider);
        authenticationManagerBuilder.userDetailsService(loginUserDetailsService);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        http
                // 기본 설정
                .csrf(AbstractHttpConfigurer::disable)
                // CORS 설정은 WebConfiguration 설정 클래스와 연동
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 인증/인가 매핑
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .requestMatchers(NOT_NEED_AUTH_APIS).permitAll()
                        .anyRequest().authenticated()
                )
                /** 아래와 같이 전역적으로 SecurityContext 저장 방식을 설정할 수 있다. */
//                .securityContext((securityContext) -> securityContext
//                        // DelegatingSecurityContextRepository로 저장 시 Request 종료 시 수동으로 Explicit Save하도록 자동 설정됨
//                        // 따라서 자동으로 save되도록 설정
//                        .securityContextRepository(new DelegatingSecurityContextRepository(
//                                new RequestAttributeSecurityContextRepository(),
//                                new HttpSessionSecurityContextRepository()
//                        ))
//                        .requireExplicitSave(false)
//                )
                .addFilterBefore(loginReqAuthFilter(http, authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .authenticationManager(authenticationManager)
                .exceptionHandling(a ->
                        a.authenticationEntryPoint(authenticationEntryPoint()))
                .exceptionHandling(e -> e
                        // 인증은 성공했지만 권한 부족으로 접근하지 못했을 경우
                        .accessDeniedHandler(new AccessDeniedByLackAuthHandler())
                )
        ;

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of(allowedIps));
        // TODO : 실제 서비스에서는 '*' 대신에 허용할 Origin을 명시적으로 설정 필요
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private LoginReqAuthFilter loginReqAuthFilter(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) {
        LoginReqAuthFilter filter = new LoginReqAuthFilter(httpSecurity);
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationFailureHandler(loginFailureHandler());
        filter.setAuthenticationSuccessHandler(loginSuccessHandler());

        return filter;
    }
}
