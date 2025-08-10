package manage.store.service.user.find;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import manage.store.dto.common.BaseResponse;
import manage.store.dto.user.find.*;
import manage.store.repository.user.UserAccountRepository;
import manage.store.service.common.mail.MailService;
import manage.store.service.user.auth.UserAuthService;
import manage.store.utils.SecretUtils;
import manage.store.exception.common.InvalidParameterException;
import manage.store.consts.Message;
import manage.store.model.common.value.SuccessFlag;
import manage.store.model.user.user.User;
import manage.store.model.user.value.OtpNo;
import manage.store.utils.ExceptionUtils;
import manage.store.utils.ReflectionUtils;
import manage.store.validator.NewPasswordValidator;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Slf4j
@Service
@RequiredArgsConstructor
public class FindUserPwServiceImpl implements FindUserPwService {

    private final UserAccountRepository userAccountRepository;

    private final MailService mailService;

    private final UserAuthService userAuthService;

    @Override
    public BaseResponse sendOtp(FindPwSendOtpRequest request) {
        // 1. 파라미터 검증
        validateFindPwParam(request);

        // 2. OTP 생성 및 user에 업데이트
        User user = userAccountRepository.selectUserById(request.getUserId());
        OtpNo otp = new OtpNo(SecretUtils.createOtp(6));
        user.setOtpNo(otp);
        userAccountRepository.updateUser(user);

        // 3. OTP 전송
        try {
            mailService.sendOtpMail(user.getEmail(), otp);
        } catch(Exception e) {
            String curMethodName = ReflectionUtils.getCurMethodName();
            log.error("[{}]Fail to send otp Mail. UserId: {}, UserEmail: {}", curMethodName, user.getId(), user.getEmail());
            log.error("[{}]ErrorMsg: {}", curMethodName, ExceptionUtils.getExceptionErrorMsg(e));

            return new BaseResponse(SuccessFlag.N, Message.FIND_PW_SEND_OTP_FAIL_FAIL_TO_SEND_OTP);
        }

        return new BaseResponse(SuccessFlag.Y, Message.FIND_PW_SEND_OTP_SUCCESS);
    }

    @Override
    public BaseResponse validateOtp(FindPwValidateOtpRequest request) {
        // 1. 파라미터 검증
        validateFindPwParam(request);

        // 2. 계정에 저장된 OTP와 동일한지 확인
        User user = userAccountRepository.selectUserById(request.getUserId());
        OtpNo originOtp = user.getOtpNo();
        boolean isOtpValid = originOtp != null && request.getOtp().equals(originOtp.value());

        return new BaseResponse(isOtpValid ? SuccessFlag.Y : SuccessFlag.N,
                isOtpValid ? Message.FIND_PW_VALIDATE_OTP_SUCCESS : Message.FIND_PW_VALIDATE_OTP_FAIL_NOT_VALID);
    }

    @Override
    public BaseResponse updatePassword(FindPwUpdatePwRequest request) {
        // 1. 파라미터 검증
        validateFindPwParam(request);

        // 2. 비밀번호 검증
        if(!NewPasswordValidator.isValid(request.getNewPassword()))
            throw new InvalidParameterException("새로운 비밀번호가 형식에 맞지 않습니다.");

        // 3. 비밀번호 암호화하여 업데이트 & OTP 초기화
        User user = userAccountRepository.selectUserById(request.getUserId());
        user.setPassword(SecretUtils.encrypt(request.getNewPassword()));
        user.setOtpNo(null);
        userAccountRepository.updateUser(user);

        return new BaseResponse(SuccessFlag.Y, Message.FIND_PW_UPDATE_PW_SUCCESS);
    }

    @Override
    public boolean isValidStep(FindUserPwSession session, FindUserPwSession.Step targetStep) {
        if(session == null || session.getCompletedStep() == null) return false;

        FindUserPwSession.Step completedStep = session.getCompletedStep();

        return targetStep.getStepOrder() <= completedStep.getStepOrder() + 1;
    }

    private void validateFindPwParam(FindPwBaseRequest request) {
        // 1. 파라미터 검증
        if(request == null ||
                !StringUtils.hasText(request.getUserId()) || !StringUtils.hasText(request.getEmail())) throw new InvalidParameterException("UserId or Email is empty");

        // 2. 계정 존재 여부 확인 (아이디와 이메일 일치)
        User user = userAccountRepository.selectUserById(request.getUserId());
        if(!userAuthService.isUserActivated(user) || !request.getEmail().equals(user.getEmail().value())) throw new InvalidParameterException("Not exist user");
    }

}
