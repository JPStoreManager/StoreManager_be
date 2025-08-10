package manage.store.api;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
@EnableWebSecurity
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 보호 비활성화 (테스트 환경에서는 보통 불필요)
                .csrf(csrf -> csrf.disable())
                // 모든 HTTP 요청에 대해 인증 없이 접근 허용
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }
}