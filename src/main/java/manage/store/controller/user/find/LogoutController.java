package manage.store.controller.user.find;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import manage.store.controller.BaseController;
import manage.store.utils.ApiPathUtils;
import manage.store.dto.common.BaseResponse;
import manage.store.utils.ExceptionUtils;
import manage.store.model.common.value.SuccessFlag;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LogoutController extends BaseController {

    @GetMapping(ApiPathUtils.ApiPath.User.LOGOUT)
    public BaseResponse logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return new BaseResponse(SuccessFlag.Y, "Logout successfully");
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse handleException(Exception e) {
        log.error("Logout failed: {}", ExceptionUtils.getExceptionErrorMsg(e));

        return new BaseResponse(SuccessFlag.N, "Logout failed..");
    }
}
