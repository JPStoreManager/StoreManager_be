package manage.store.service.user.login;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import manage.store.dto.user.login.LoginRequest;
import manage.store.dto.user.login.LoginResponse;
import manage.store.model.common.value.SuccessFlag;
import manage.store.model.user.user.User;
import manage.store.repository.user.UserAccountRepository;
import manage.store.service.user.auth.UserAuthService;
import manage.store.utils.SecretUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserAccountRepository userAccountRepository;

    private final UserAuthService userAuthService;

    @Override
    public LoginResponse login(@Valid LoginRequest loginDTO) {
        // 1. 아이디를 통해 사용자 계정 조회
        // 만약 없으면 실패 return
        User user = userAccountRepository.selectUserById(loginDTO.getId());
        if(!userAuthService.isUserActivated(user)) return new LoginResponse(SuccessFlag.N);

        // 2. 전달된 비밀번호가 암호화된 비밀번호와 일치하는지 확인
        if(SecretUtils.verify(loginDTO.getPassword(), user.getPassword())) return new LoginResponse(SuccessFlag.Y);
        else return new LoginResponse(SuccessFlag.N);
    }

}
