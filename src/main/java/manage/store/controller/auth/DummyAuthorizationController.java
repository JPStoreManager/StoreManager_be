package manage.store.controller.auth;

import manage.store.consts.Message;
import manage.store.dto.auth.AuthMeResult;
import manage.store.dto.common.ApiResponse;
import manage.store.model.common.value.SuccessFlag;
import manage.store.service.user.auth.model.LoginUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyAuthorizationController {

    @GetMapping("/auth/me")
    public ResponseEntity<ApiResponse<AuthMeResult>> checkAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        AuthMeResult result = new AuthMeResult(((LoginUserDetails) authentication.getPrincipal()).getUserId());

        return ResponseEntity.ok(ApiResponse.success(result, "사용자 인증 정보 확인 성공"));
    }

}
