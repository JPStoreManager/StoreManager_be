package manage.store.api.controller;

import com.google.gson.Gson;
import manage.store.StoreManagerApplication;
import manage.store.controller.user.find.FindUserPwController;
import manage.store.dto.common.BaseResponse;
import manage.store.dto.user.find.FindPwSendOtpRequest;
import manage.store.dto.user.find.FindPwUpdatePwRequest;
import manage.store.dto.user.find.FindPwValidateOtpRequest;
import manage.store.dto.user.find.FindUserPwSession;
import manage.store.service.user.find.FindUserPwService;
import manage.store.service.user.session.FindUserPwSessionService;
import manage.store.utils.ApiPathUtils;
import manage.store.consts.Message;
import manage.store.consts.Tags;
import manage.store.exception.common.InvalidParameterException;
import manage.store.model.common.value.SuccessFlag;
import manage.store.model.user.user.User;
import manage.store.model.user.value.Email;
import manage.store.model.user.value.UserId;
import manage.store.testUtils.user.UserData;
import manage.store.testUtils.util.MockMvcUtils;
import manage.store.utils.GsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag(Tags.Test.UNIT)
@ContextConfiguration(classes = StoreManagerApplication.class)
@WebMvcTest( controllers = FindUserPwController.class)
class FindUserPwControllerTest {

    private MockMvc mock;

    @MockBean
    private FindUserPwService findUserPwService;

    @MockBean
    private FindUserPwSessionService findUserPwSessionService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setUp() {
        mock = MockMvcUtils.configureDefaultMockMvc(context).build();
    }

    /**
     * sendOtp
     */
    private static final String SEND_OTP_PATH = ApiPathUtils.getPath(ApiPathUtils.ApiName.FIND_PW_SEND_OTP);

    @Test
    @DisplayName("sendOtp 성공")
    void sendOtp_success() throws Exception {
        // Given
        final User user = UserData.user1();
        final FindPwSendOtpRequest request = new FindPwSendOtpRequest();
        request.setUserId(user.getId().value());
        request.setEmail(user.getEmail().value());
        final String SESSION_ID = "sessionKey";

        given(findUserPwService.sendOtp(any()))
                .willReturn(new BaseResponse(SuccessFlag.Y, Message.FIND_PW_SEND_OTP_SUCCESS));

        given(findUserPwSessionService.createSessionKey()).willReturn(SESSION_ID);

        doNothing().when(findUserPwSessionService).updateSession(SESSION_ID, request, FindUserPwSession.Step.SEND_OTP);

        // When - Then
        Gson gson = GsonUtils.getGson();
        mock.perform(post(SEND_OTP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(SuccessFlag.Y.getValue()))
                .andExpect(jsonPath("$.msg").value(Message.FIND_PW_SEND_OTP_SUCCESS))
                .andExpect(jsonPath("$.sessionId").value(SESSION_ID));
    }

    @Test
    @DisplayName("sendOtp 실패 - 유효하지 않은 파리미터")
    void sendOtp_fail_invalidParameters() throws Exception {
        // Given
        final User user = UserData.user1();
        final String[][] params = {
                {null, null},

                {user.getId().value(), null},
                {user.getId().value(), ""},
                {user.getId().value(), "  "},

                {null, user.getEmail().value()},
                {"", user.getEmail().value()},
                {"  ", user.getEmail().value()},

                {user.getId().value(), "wrongEmail1"},
                {user.getId().value(), "wro@a"},
                {user.getId().value(), "wro@.c"},
                {user.getId().value(), "@abc.com"},
        };

        // When - Then
        Gson gson = GsonUtils.getGson();
        for (String[] param : params) {
            FindPwSendOtpRequest request = new FindPwSendOtpRequest();
            request.setUserId(param[0]);
            request.setEmail(param[1]);

            mock.perform(post(SEND_OTP_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(gson.toJson(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));
        }
    }

    @Test
    @DisplayName("sendOtp 실패 - 존재하지 않는 계정")
    void sendOtp_fail_notExistUser() throws Exception {
        // Given
        final User user = UserData.user1();
        final FindPwSendOtpRequest request = new FindPwSendOtpRequest();
        request.setUserId(user.getId().value());
        request.setEmail(user.getEmail().value());

        given(findUserPwService.sendOtp(any())).willThrow(new InvalidParameterException());

        // When - Then
        Gson gson = GsonUtils.getGson();
        mock.perform(post(SEND_OTP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));
    }

    @Test
    @DisplayName("sendOtp 실패 - 메일 전송 실패")
    void sendOtp_fail_sendMailFail() throws Exception {
        // Given
        final User user = UserData.user1();
        final FindPwSendOtpRequest request = new FindPwSendOtpRequest();
        request.setUserId(user.getId().value());
        request.setEmail(user.getEmail().value());

        given(findUserPwService.sendOtp(any()))
                .willReturn(new BaseResponse(SuccessFlag.N, Message.FIND_PW_SEND_OTP_FAIL_FAIL_TO_SEND_OTP));

        // When - Then
        Gson gson = GsonUtils.getGson();
        mock.perform(post(SEND_OTP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));
    }

    /**
     * validateOtp
     */
    private static final String VALIDATE_OTP_PATH = ApiPathUtils.getPath(ApiPathUtils.ApiName.FIND_PW_VALIDATE_OTP);
    private static final String FIND_PW_HEADER_ID = "JP_FPW_ID";
    @Test
    @DisplayName("validateOtp 성공")
    void validateOtp_success() throws Exception {
        // Given
        final User user = UserData.user1();
        final FindPwValidateOtpRequest request = new FindPwValidateOtpRequest();
        request.setUserId(user.getId().value());
        request.setEmail(user.getEmail().value());
        request.setOtp(user.getOtpNo().value());
        final String SESSION_ID = "sessionKey";


        FindUserPwSession session = new FindUserPwSession(FindUserPwSession.Step.SEND_OTP, new UserId(request.getUserId()), new Email(request.getEmail()));
        given(findUserPwSessionService.getSession(SESSION_ID)).willReturn(session);
        given(findUserPwService.isValidStep(session, FindUserPwSession.Step.VALIDATE_OTP)).willReturn(true);
        given(findUserPwService.validateOtp(any()))
                .willReturn(new BaseResponse(SuccessFlag.Y, Message.FIND_PW_VALIDATE_OTP_SUCCESS));


        doNothing().when(findUserPwSessionService).updateSession(SESSION_ID, request, FindUserPwSession.Step.VALIDATE_OTP);

        // When - Then
        Gson gson = GsonUtils.getGson();
        mock.perform(post(VALIDATE_OTP_PATH)
                .header(FIND_PW_HEADER_ID, SESSION_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(SuccessFlag.Y.getValue()))
                .andExpect(jsonPath("$.msg").value(Message.FIND_PW_VALIDATE_OTP_SUCCESS))
                .andExpect(jsonPath("$.sessionId").value(SESSION_ID));
    }

    @Test
    @DisplayName("validateOtp 실패 - 필수 헤더 JP_FPW_ID 없음")
    void validateOtp_fail_noHeader() throws Exception {
        // Given
        final User user = UserData.user1();
        final FindPwValidateOtpRequest request = new FindPwValidateOtpRequest();
        request.setUserId(user.getId().value());
        request.setEmail(user.getEmail().value());
        request.setOtp(user.getOtpNo().value());

        // When - Then
        Gson gson = GsonUtils.getGson();
        mock.perform(post(VALIDATE_OTP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));
    }


    @Test
    @DisplayName("validateOtp 실패 - 세션 없음")
    void validateOtp_fail_noSession() throws Exception {
        // Given
        final User user = UserData.user1();
        final FindPwValidateOtpRequest request = new FindPwValidateOtpRequest();
        request.setUserId(user.getId().value());
        request.setEmail(user.getEmail().value());
        request.setOtp(user.getOtpNo().value());
        final String SESSION_ID = "sessionId";

        given(findUserPwSessionService.getSession(any())).willReturn(null);

        // When - Then
        Gson gson = GsonUtils.getGson();
        mock.perform(post(VALIDATE_OTP_PATH)
                .header(FIND_PW_HEADER_ID, SESSION_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));
    }

    @Test
    @DisplayName("validateOtp 실패 - 세션 단계 미완료")
    void validateOtp_fail_notCompleteStep() throws Exception {
        // Given
        final User user = UserData.user1();
        final FindPwValidateOtpRequest request = new FindPwValidateOtpRequest();
        request.setUserId(user.getId().value());
        request.setEmail(user.getEmail().value());
        request.setOtp(user.getOtpNo().value());
        final String SESSION_ID = "sessionId";

        given(findUserPwSessionService.getSession(SESSION_ID))
                .willReturn(new FindUserPwSession(FindUserPwSession.Step.NONE, new UserId(request.getUserId()), new Email(request.getEmail())));

        // When - Then
        Gson gson = GsonUtils.getGson();
        mock.perform(post(VALIDATE_OTP_PATH)
                .header(FIND_PW_HEADER_ID, SESSION_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));

    }

    @Test
    @DisplayName("validateOtp 실패 - 잘못된 파라미터")
    void validateOtp_fail_invalidParameters() throws Exception {
        // Given
        final User user = UserData.user1();
        final String[][] params = {
                {null, null, null},

                {null, user.getEmail().value(), user.getOtpNo().value()},
                {"", user.getEmail().value(), user.getOtpNo().value()},
                {" ", user.getEmail().value(), user.getOtpNo().value()},

                {user.getId().value(), null, user.getOtpNo().value()},
                {user.getId().value(), "", user.getOtpNo().value()},
                {user.getId().value(), " ", user.getOtpNo().value()},

                {user.getId().value(), user.getEmail().value(), null},
                {user.getId().value(), user.getEmail().value(), ""},
                {user.getId().value(), user.getEmail().value(), " "},

                {user.getId().value(), "a", "12345"},
                {user.getId().value(), "a@", "12345"},
                {user.getId().value(), "a@b", "12345"},
                {user.getId().value(), "a@b.", "12345"},
                {user.getId().value(), "a@.c", "12345"},
                {user.getId().value(), "@b.c", "12345"},
                {user.getId().value(), "@.c", "12345"},
        };
        final String SESSION_ID = "sessionId";

        // When - Then
        for (String[] param : params) {
            final FindPwValidateOtpRequest request = new FindPwValidateOtpRequest();
            request.setUserId(param[0]);
            request.setEmail(param[1]);
            request.setOtp(param[2]);

            Gson gson = GsonUtils.getGson();
            mock.perform(post(VALIDATE_OTP_PATH)
                    .header(FIND_PW_HEADER_ID, SESSION_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));
        }

    }

    @Test
    @DisplayName("validateOtp 실패 - 존재하지 않는 사용자 / 계정 이메일과의 불일치")
    void validateOtp_fail_notExistUser() throws Exception {
        // Given
        final User user = UserData.user1();
        final FindPwValidateOtpRequest request = new FindPwValidateOtpRequest();
        request.setUserId(user.getId().value());
        request.setEmail(user.getEmail().value());
        request.setOtp(user.getOtpNo().value());
        final String SESSION_ID = "sessionId";

        given(findUserPwSessionService.getSession(SESSION_ID))
                .willReturn(new FindUserPwSession(FindUserPwSession.Step.SEND_OTP, new UserId(request.getUserId()), new Email(request.getEmail())));

        given(findUserPwService.validateOtp(any())).willThrow(new InvalidParameterException());

        // When - Then
        Gson gson = GsonUtils.getGson();
        mock.perform(post(VALIDATE_OTP_PATH)
                .header(FIND_PW_HEADER_ID, SESSION_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));

    }

    @Test
    @DisplayName("validateOtp 실패 - OTP 검증 실패")
    void validateOtp_fail_notValidOtp() throws Exception {
        // Given
        final User user = UserData.user1();
        final FindPwValidateOtpRequest request = new FindPwValidateOtpRequest();
        request.setUserId(user.getId().value());
        request.setEmail(user.getEmail().value());
        request.setOtp(user.getOtpNo().value());
        final String SESSION_ID = "sessionId";

        FindUserPwSession session = new FindUserPwSession(FindUserPwSession.Step.SEND_OTP, new UserId(request.getUserId()), new Email(request.getEmail()));
        given(findUserPwSessionService.getSession(SESSION_ID))
                .willReturn(session);
        given(findUserPwService.isValidStep(session, FindUserPwSession.Step.VALIDATE_OTP)).willReturn(true);
        given(findUserPwService.validateOtp(any())).willReturn(new BaseResponse(SuccessFlag.N, Message.FIND_PW_VALIDATE_OTP_FAIL_NOT_VALID));

        // When - Then
        mock.perform(post(VALIDATE_OTP_PATH)
                .header(FIND_PW_HEADER_ID, SESSION_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));
    }

    /**
     * updatePassword
     */
    private static final String UPDATE_PW_PATH = ApiPathUtils.getPath(ApiPathUtils.ApiName.FIND_PW_UPDATE_PW);

    @Test
    @DisplayName("updatePassword 성공")
    void updatePassword_success() throws Exception {
        // Given
        final User user = UserData.user1();
        final FindPwUpdatePwRequest request = new FindPwUpdatePwRequest();
        request.setUserId(user.getId().value());
        request.setEmail(user.getEmail().value());
        request.setNewPassword("1234qwer");
        final String sessionId = "SESSION_ID";

        FindUserPwSession session = new FindUserPwSession(FindUserPwSession.Step.VALIDATE_OTP, new UserId(request.getUserId()), new Email(request.getEmail()));
        given(findUserPwSessionService.getSession(sessionId)).willReturn(session);
        given(findUserPwService.isValidStep(session, FindUserPwSession.Step.NEW_PW)).willReturn(true);
        given(findUserPwService.updatePassword(any())).willReturn(new BaseResponse(SuccessFlag.Y, Message.FIND_PW_UPDATE_PW_SUCCESS));
        doNothing().when(findUserPwSessionService).removeSession(sessionId);

        // When - Then
        Gson gson = GsonUtils.getGson();
        mock.perform(put(UPDATE_PW_PATH)
                .header(FIND_PW_HEADER_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(SuccessFlag.Y.getValue()));
    }

    @Test
    @DisplayName("updatePassword 실패 - 필수 헤더 JP_FPW_ID 없음")
    void updatePassword_fail_noHeader() throws Exception {
        // Given
        final User user = UserData.user1();
        final FindPwUpdatePwRequest request = new FindPwUpdatePwRequest();
        request.setUserId(user.getId().value());
        request.setEmail(user.getEmail().value());
        request.setNewPassword("1234qwer");

        // When - Then
        Gson gson = GsonUtils.getGson();
        mock.perform(put(UPDATE_PW_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));
    }

    @Test
    @DisplayName("updatePassword 실패 - 잘못된 파라미터")
    void updatePassword_fail_invalidParam() throws Exception {
        // Given
        final User user = UserData.user1();
        final String SESSION_ID = "sessionId";
        final String[][] params = {
                {null, null, null},

                {user.getId().value(), user.getEmail().value(), null},
                {user.getId().value(), user.getEmail().value(), ""},
                {user.getId().value(), user.getEmail().value(), " "},

                {user.getId().value(), user.getEmail().value(), "12345678"},
                {user.getId().value(), user.getEmail().value(), "qwerabcd"},
                {user.getId().value(), user.getEmail().value(), "1234!@#$"},
                {user.getId().value(), user.getEmail().value(), "qwer!@#$"},
                {user.getId().value(), user.getEmail().value(), "!@#$%^&*"},

                {null, user.getEmail().value(), "1234qwer"},
                {"", user.getEmail().value(), "1234qwer"},
                {" ", user.getEmail().value(), "1234qwer"},

                {user.getId().value(), null, "1234qwer"},
                {user.getId().value(), "", "1234qwer"},
                {user.getId().value(), " ", "1234qwer"},
        };

        // When - Then
        Gson gson = GsonUtils.getGson();
        for (String[] param : params) {
            FindPwUpdatePwRequest request = new FindPwUpdatePwRequest();
            request.setUserId(param[0]);
            request.setEmail(param[1]);
            request.setNewPassword(param[2]);

            mock.perform(put(UPDATE_PW_PATH)
                    .header(FIND_PW_HEADER_ID, SESSION_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));
        }
    }

    @Test
    @DisplayName("updatePassword 실패 - 잘못된 접근")
    void updatePassword_fail_invalidAccess() throws Exception {
        // Given
        final User user = UserData.user1();
        final FindPwUpdatePwRequest request = new FindPwUpdatePwRequest();
        request.setUserId(user.getId().value());
        request.setEmail(user.getEmail().value());
        request.setNewPassword("1234qwer");
        final String sessionId = "SESSION_ID";

        FindUserPwSession session = new FindUserPwSession(FindUserPwSession.Step.NONE, new UserId(request.getUserId()), new Email(request.getEmail()));
        given(findUserPwSessionService.getSession(sessionId)).willReturn(session);
        given(findUserPwService.isValidStep(session, FindUserPwSession.Step.NEW_PW)).willReturn(false);

        // When - Then
        Gson gson = GsonUtils.getGson();
        mock.perform(put(UPDATE_PW_PATH)
                .header(FIND_PW_HEADER_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));
    }

    @Test
    @DisplayName("updatePassword 실패 - 업데이트 실패")
    void updatePassword_fail_updateFail() throws Exception {
        // Given
        final User user = UserData.user1();
        final FindPwUpdatePwRequest request = new FindPwUpdatePwRequest();
        request.setUserId(user.getId().value());
        request.setEmail(user.getEmail().value());
        request.setNewPassword("1234qwer");
        final String sessionId = "SESSION_ID";

        FindUserPwSession session = new FindUserPwSession(FindUserPwSession.Step.VALIDATE_OTP, new UserId(request.getUserId()), new Email(request.getEmail()));
        given(findUserPwSessionService.getSession(sessionId)).willReturn(session);
        given(findUserPwService.isValidStep(session, FindUserPwSession.Step.NEW_PW)).willReturn(true);
        given(findUserPwService.updatePassword(any())).willReturn(new BaseResponse(SuccessFlag.N, Message.FIND_PW_UPDATE_PW_FAIL_INVALID_PW));

        // When - Then
        Gson gson = GsonUtils.getGson();
        mock.perform(put(UPDATE_PW_PATH)
                .header(FIND_PW_HEADER_ID, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));
    }


}