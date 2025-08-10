package manage.store.service.user.auth;

import lombok.RequiredArgsConstructor;
import manage.store.model.user.user.User;
import manage.store.service.user.auth.model.LoginUserDetails;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

    @Override
    public boolean isUserActivated(User user) {
        return user != null && !user.isDeleted();
    }

    @Override
    public LoginUserDetails getLoginUserDetails() throws AuthenticationServiceException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) throw new AuthenticationServiceException("Authentication is null");

        Object principal = authentication.getPrincipal();
        if(!(principal instanceof LoginUserDetails)) {
            throw new AuthenticationServiceException("Principal is not an instance of LoginUserDetails");
        }

        return (LoginUserDetails) principal;
    }

    @Override
    public boolean isUserAuthenticated() throws AuthenticationServiceException {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            // 인증 정보가 없거나 인증되지 않은 경우 false 반환
            if(authentication == null || !authentication.isAuthenticated()) return false;

            LoginUserDetails userDetails = getLoginUserDetails();
            return userDetails != null;
        } catch (AuthenticationServiceException e) {
            return false; // 인증 정보가 없거나 잘못된 경우 false 반환
        }
    }

}
