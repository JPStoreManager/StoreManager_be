package manage.store.config.auth.abac;

import lombok.RequiredArgsConstructor;
import manage.store.service.auth.policy.branch.BranchAccessPolicy;
import manage.store.service.auth.policy.sales.SalesAccessPolicy;
import manage.store.service.user.auth.UserAuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * Access-Based Access Control용 Policy Bean 설정 클래스
 */
@Configuration
@EnableMethodSecurity // @PreAuthorize와 같은 메서드 단위 검증 애노테이션 활성화
@RequiredArgsConstructor
public class AbacPolicyConfig {

    private final UserAuthService userAuthService;

    @Bean
    public SalesAccessPolicy salesAccessPolicy() {
        return new SalesAccessPolicy(userAuthService);
    }

    @Bean
    public BranchAccessPolicy branchAccessPolicy() {
        return new BranchAccessPolicy(userAuthService);
    }

}
