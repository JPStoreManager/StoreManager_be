package manage.store.controller.user.find;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import manage.store.controller.BaseController;
import manage.store.consts.Message;
import manage.store.dto.common.ApiResponse;
import manage.store.dto.common.BaseResult;
import manage.store.dto.user.find.*;
import manage.store.service.user.find.FindUserPwService;
import manage.store.service.user.session.FindUserPwSessionService;
import manage.store.utils.ApiPathUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import manage.store.exception.common.InvalidParameterException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FindUserPwController extends BaseController {

    private static final String FIND_PW_HEADER_ID = "jp_fpw_id";

    private final FindUserPwService findUserPwService;

    private final FindUserPwSessionService findUserPwSessionService;

    /**
     * 비밀번호 찾기 - OTP 발송 <br>
     * @param request userId {@code String, mandatory}: 사용자 아이디 <br>
     *                email {@code String, mandatory}: 사용자 이메일 <br>
     * @return result SuccessFlag - 계정 인증 성공: Y <br>
     * 계정 인증 실패 / 메일 전송 실패: N <br>
     * msg String - 성공 / 실패에 대한 메세지 <br>
     * @throws InvalidParameterException 사용자가 입력한 id와 email이 존재하지 않거나 유효하지 않을 경우
     */
    @PostMapping(ApiPathUtils.ApiPath.User.FindPassword.SEND_OTP)
    public ResponseEntity<ApiResponse<FindPwSendOtpResponse>> sendOtp(@RequestBody @Valid FindPwSendOtpRequest request) {
        BaseResult result = findUserPwService.sendOtp(request);

        if(result.isSuccess()) {
            String sessionKey = findUserPwSessionService.createSessionKey();
            findUserPwSessionService.updateSession(sessionKey, request, FindUserPwSession.Step.SEND_OTP);

            return ResponseEntity.ok(ApiResponse.success(FindPwSendOtpResponse.success(sessionKey), Message.FIND_PW_SEND_OTP_SUCCESS));
        }

        return ResponseEntity.ok(ApiResponse.fail(Message.FIND_PW_SEND_OTP_FAIL_FAIL_TO_SEND_OTP));
    }

    /**
     * 비밀번호 찾기 - OTP 검증 <br>
     * @header JP_FPW_ID 비밀번호 찾기 세션 아이디
     * @param request userId {@code String, mandatory}: 사용자 아이디 <br>
     *                email {@code String, mandatory}: 사용자 이메일 <br>
     *                otp {@code String, mandatory}: 사용자가 입력한 OTP 번호
     * @return result {@code SuccessFlag} OTP 검증 성공 여부 <br>
     * - OTP 검증 성공: Y <br>
     * - OTP 검증 실패: N <br>
     * msg{@code String} - 성공 / 실패에 대한 메세지 <br>
     */
    @PostMapping(ApiPathUtils.ApiPath.User.FindPassword.VALIDATE_OTP)
    public ResponseEntity<ApiResponse<FindPwValidateOtpResponse>> validateOtp(@RequestHeader(value = FIND_PW_HEADER_ID) String sessionId,
                                      @RequestBody @Valid FindPwValidateOtpRequest request) {
        // 1. otp 전송 단계를 거쳤는지 session을 통해 검증
        FindUserPwSession session = findUserPwSessionService.getSession(sessionId);
        if(!findUserPwService.isValidStep(session, FindUserPwSession.Step.VALIDATE_OTP)) {
            return ResponseEntity.badRequest().body(ApiResponse.fail(Message.FIND_PW_FAIL_INVALID_PARAM_OR_ACCESS));
        }

        // 2. otp 검증
        BaseResult result = findUserPwService.validateOtp(request);
        if(result.isSuccess()) {
            // 3. otp 검증 성공 시 session 업데이트
            findUserPwSessionService.updateSession(sessionId, request, FindUserPwSession.Step.VALIDATE_OTP);

            return ResponseEntity.ok(ApiResponse.success(FindPwValidateOtpResponse.success(sessionId), Message.FIND_PW_VALIDATE_OTP_SUCCESS));
        }

        // 실패 응답 반환
        return ResponseEntity.ok(ApiResponse.fail(Message.FIND_PW_VALIDATE_OTP_FAIL_NOT_VALID));
    }

    /**
     * 비밀번호 찾기 - 비밀번호 업데이트 <br>
     * @header JP_FPW_ID - 비밀번호 찾기 세션 아이디
     * @param request userId {@code String, mandatory}: 사용자 아이디 <br>
     *                email {@code String, mandatory}: 사용자 이메일 <br>
     *                newPassword {@code String, mandatory}: 사용자가 신규로 입력한 비밀번호
     * @return result {@code SuccessFlag} - 비밀번호 업데이트 성공 시 Y, 잘못된 비밀번호일 시 N <br>
     *         msg {@code String} - 성공 / 실패에 대한 메세지
     */
    @PutMapping(ApiPathUtils.ApiPath.User.FindPassword.UPDATE_PW)
    public ResponseEntity<ApiResponse> updatePassword(@RequestHeader(value = FIND_PW_HEADER_ID) String sessionId,
                                                                 @RequestBody @Valid FindPwUpdatePwRequest request) {
        // 1. otp 검증 단계를 거쳤는지 session을 통해 검증
        FindUserPwSession session = findUserPwSessionService.getSession(sessionId);
        if(!findUserPwService.isValidStep(session, FindUserPwSession.Step.NEW_PW)) {
            return ResponseEntity.badRequest().body(ApiResponse.fail(Message.FIND_PW_FAIL_INVALID_PARAM_OR_ACCESS));
        }

        // 2. 비밀번호 업데이트
        BaseResult result = findUserPwService.updatePassword(request);
        if(result.isSuccess()) {
            // 3. 세션 비활성화
            findUserPwSessionService.removeSession(sessionId);
            return ResponseEntity.ok(ApiResponse.success(Message.FIND_PW_UPDATE_PW_SUCCESS));
        }

        // 4. 실패 응답 반환
        return ResponseEntity.ok(ApiResponse.fail(Message.FIND_PW_UPDATE_PW_FAIL_INVALID_PW));
    }

}
