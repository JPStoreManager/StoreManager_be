package manage.store.api.integration.find;

import com.google.gson.Gson;
import manage.store.StoreManagerApplication;
import manage.store.dto.common.ApiResponse;
import manage.store.dto.user.find.*;
import manage.store.repository.user.account.mapper.UserAccountMapper;
import manage.store.utils.ApiPathUtils;
import manage.store.model.common.value.SuccessFlag;
import manage.store.model.user.user.User;
import manage.store.consts.Profiles;
import manage.store.consts.Tags;
import manage.store.api.integration.BaseIntegration;
import manage.store.testUtils.user.UserData;
import manage.store.utils.GsonUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.google.common.reflect.TypeToken;

import java.lang.reflect.Type;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag(Tags.Test.INTEGRATION)
@Transactional
//@Testcontainers
@SpringBootTest(classes = StoreManagerApplication.class)
@ActiveProfiles(Profiles.TEST)
@ExtendWith({RestDocumentationExtension.class})
public class FindPwValidateOtpTest extends BaseIntegration {

    private static final String SEND_OTP_PATH = ApiPathUtils.getPath(ApiPathUtils.ApiName.FIND_PW_SEND_OTP);
    private static final String VALIDATE_OTP_PATH = ApiPathUtils.getPath(ApiPathUtils.ApiName.FIND_PW_VALIDATE_OTP);

    @Autowired
    private UserAccountMapper mapper;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private User user;

    /** Docker container for Test */
//    @Container
//    private static final DockerComposeContainer composeContainer = getDockerComposeContainer();
//    {
//        composeContainer.start();
//    }

    @BeforeEach
    public void setup(TestInfo testInfo, RestDocumentationContextProvider restDocument) {
        mockMvc = getMockMvc(testInfo, context, restDocument);

        user = UserData.user1();
        mapper.insertUser(user);
    }


    /** validateOtp */
    @Test
    @Tag(Tags.Test.DOCS)
    @DisplayName("validateOtp 성공")
    public void validateOtp_success() throws Exception {
        // Given
        // 1) sendOtp
        final FindPwSendOtpRequest sendOtpRequest = new FindPwSendOtpRequest();
        sendOtpRequest.setUserId(user.getId().value());
        sendOtpRequest.setEmail(user.getEmail().value());

        // 1차로 otp 전송 단계로 통과 검증을 위한 세션 발급받기
        Gson gson = GsonUtils.getGson();
        MvcResult sendOtpResult = mockMvc.perform(post(SEND_OTP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(sendOtpRequest))).andReturn();

        Type sendOtpResponseType = new TypeToken<ApiResponse<FindPwSendOtpResponse>>() {}.getType();
        ApiResponse<FindPwSendOtpResponse> sendOtpResponse = gson.fromJson(sendOtpResult.getResponse().getContentAsString(), sendOtpResponseType);

        // 2) validateOtp
        String otpNo = mapper.selectUserById(user.getId().value()).getOtpNo().value();
        final String sessionId = sendOtpResponse.getData().getSessionId();

        final FindPwValidateOtpRequest validateOtpRequest = new FindPwValidateOtpRequest();
        validateOtpRequest.setUserId(user.getId().value());
        validateOtpRequest.setEmail(user.getEmail().value());
        validateOtpRequest.setOtp(otpNo);

        final MockHttpSession session = new MockHttpSession();
        session.setAttribute(sessionId, new FindUserPwSession(FindUserPwSession.Step.SEND_OTP, user.getId(), user.getEmail()));

        ResultActions result = mockMvc.perform(
                post(VALIDATE_OTP_PATH)
                        .session(session)
                        .header("JP_FPW_ID", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(validateOtpRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(SuccessFlag.Y.getValue()))
                .andExpect(jsonPath("$.data.sessionId").value(sessionId));
        addDocs(result);
    }

    @Test
    @DisplayName("validateOtp 실패 - 잘못된 파라미터")
    public void validateOtp_fail_invalidParameter() throws Exception {
        // Given
        final String otp = "123456";
        final String[][] params = {
                {null, null, null},

                {user.getId().value(), null, otp},
                {user.getId().value(), "", otp},
                {user.getId().value(), " ", otp},

                {null, user.getEmail().value(), otp},
                {"", user.getEmail().value(), otp},
                {" ", user.getEmail().value(), otp},

                {user.getId().value(), user.getEmail().value(), null},
                {user.getId().value(), user.getEmail().value(), ""},
                {user.getId().value(), user.getEmail().value(), " "},
        };

        // When - Then
        for(String[] param : params) {
            final FindPwValidateOtpRequest request = new FindPwValidateOtpRequest();
            request.setUserId(param[0]);
            request.setEmail(param[1]);
            request.setOtp(param[2]);

            Gson gson = GsonUtils.getGson();
            mockMvc.perform(post(VALIDATE_OTP_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));
        }
    }

    @Test
    @DisplayName("validateOtp 실패 - 잘못된 접근")
    public void validateOtp_fail_invalidAccess() throws Exception {
        // Given
        final FindPwValidateOtpRequest request = new FindPwValidateOtpRequest();
        request.setUserId(user.getId().value());
        request.setEmail(user.getEmail().value());
        request.setOtp("123456");

        // When
        Gson gson = GsonUtils.getGson();
        mockMvc.perform(
                post(VALIDATE_OTP_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));
    }

    @Test
    @DisplayName("validateOtp 실패 - OTP 검증 실패")
    public void validateOtp_fail_invalidOtp() throws Exception {
        // Given
        final FindPwSendOtpRequest sendOtpRequest = new FindPwSendOtpRequest();
        sendOtpRequest.setUserId(user.getId().value());
        sendOtpRequest.setEmail(user.getEmail().value());

        // 1차로 otp 전송 단계로 통과 검증을 위한 세션 발급받기
        Gson gson = GsonUtils.getGson();
        MvcResult sendOtpResult = mockMvc.perform(post(SEND_OTP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(sendOtpRequest))).andReturn();

        Type sendOtpResponseType = new TypeToken<ApiResponse<FindPwSendOtpResponse>>() {}.getType();
        ApiResponse<FindPwSendOtpResponse> sendOtpResponse = gson.fromJson(sendOtpResult.getResponse().getContentAsString(), sendOtpResponseType);

        // 2) validateOtp
        final String sessionId = sendOtpResponse.getData().getSessionId();

        final FindPwValidateOtpRequest validateOtpRequest = new FindPwValidateOtpRequest();
        validateOtpRequest.setUserId(user.getId().value());
        validateOtpRequest.setEmail(user.getEmail().value());
        String dbOtp = mapper.selectUserById(user.getId().value()).getOtpNo().value();
        validateOtpRequest.setOtp(dbOtp + "1");

        final MockHttpSession session = new MockHttpSession();
        session.setAttribute(sessionId, new FindUserPwSession(FindUserPwSession.Step.SEND_OTP, user.getId(), user.getEmail()));

        ResultActions result = mockMvc.perform(
                post(VALIDATE_OTP_PATH)
                        .session(session)
                        .header("JP_FPW_ID", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(validateOtpRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));
    }


    @Override
    protected void addDocs(ResultActions result) throws Exception {
        result.andDo(document("findPassword/validateOtp",
                requestHeaders(
                        headerWithName("JP_FPW_ID").description("비밀번호 찾기 세션 ID")
                ),
                requestFields(
                        fieldWithPath("userId").description("사용자 아이디"),
                        fieldWithPath("email").description("사용자 이메일"),
                        fieldWithPath("otp").description("사용자가 입력한 OTP 번호")

                ),
                responseFields(
                        fieldWithPath("result").description("OTP 검증 성공 여부"),
                        fieldWithPath("msg").description("성공 / 실패에 대한 메세지"),
                        fieldWithPath("data").description("응답 데이터").type(FindPwValidateOtpResponse.class),
                        fieldWithPath("data.sessionId").description("비밀번호 찾기에 활용되는 세션 ID"),
                        fieldWithPath("timestamp").description("API 응답일시")
                )));
    }
}
