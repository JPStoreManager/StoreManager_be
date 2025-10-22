package manage.store.service.user.login;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import manage.store.dto.common.BaseResult;
import manage.store.dto.user.login.LoginRequest;
import manage.store.model.user.user.User;
import manage.store.repository.user.account.UserAccountRepository;
import manage.store.service.user.auth.UserAuthService;
import manage.store.utils.SecretUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserAccountRepository userAccountRepository;

    private final UserAuthService userAuthService;

    @Override
    public BaseResult login(@Valid LoginRequest loginDTO) {
        // 1. 아이디를 통해 사용자 계정 조회
        User user = userAccountRepository.selectUserById(loginDTO.getId());
        if(!userAuthService.isUserActivated(user)) {
            return BaseResult.fail("비활성화된 인원입니다.");
        }

        // 2. 전달된 비밀번호가 암호화된 비밀번호와 일치하는지 확인
        if(SecretUtils.verify(loginDTO.getPassword(), user.getPassword())) return BaseResult.success();
        else return BaseResult.fail("비밀번호가 일치하지 않습니다.");
    }

}
