package manage.store.controller.auth;

import manage.store.consts.Message;
import manage.store.dto.auth.AuthMeResponse;
import manage.store.model.common.value.SuccessFlag;
import manage.store.service.user.auth.model.LoginUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyAuthorizationController {

    @GetMapping("/auth/me")
    public AuthMeResponse checkAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return new AuthMeResponse(SuccessFlag.Y, Message.AUTH_ME_SUCCESS, ((LoginUserDetails) authentication.getPrincipal()).getUserId().value());
    }

}
