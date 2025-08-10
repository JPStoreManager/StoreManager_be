package manage.store.consts;

// TODO 메세지 임시 하드 코딩. 추후 DB화 필요
public interface Message {

    /** 작명 방법: {기능}_{SUCCESS/FAIL}_{상세내용} */

    /** 로그인 */
    String LOGIN_SUCCESS = "로그인 성공";
    String LOGIN_FAIL_INVALID_PARAM = "올바른 정보를 입력하세요.";
    String LOGIN_FAIL_NOT_EXIST_USER = "존재하지 아이디 이거나 비밀번호가 올바르지 않습니다";

    /** 비밀번호 찾기 */
    String FIND_PW_FAIL_INVALID_PARAM_OR_ACCESS = "올바른 정보를 입력하지 않았거나 잘못된 접근입니다.";
    String FIND_PW_SEND_OTP_SUCCESS = "OTP 전송 성공";
    String FIND_PW_SEND_OTP_FAIL_FAIL_TO_SEND_OTP = "OTP 메일 전송에 실패하였습니다.";
    String FIND_PW_VALIDATE_OTP_SUCCESS = "OTP 인증 성공";
    String FIND_PW_VALIDATE_OTP_FAIL_NOT_VALID = "OTP 인증 실패";
    String FIND_PW_UPDATE_PW_SUCCESS = "비밀번호 업데이트 성공";
    String FIND_PW_UPDATE_PW_FAIL_INVALID_PW = "올바르지 않는 비밀번호입니다.";

    /** 인증 */
    String AUTH_ME_SUCCESS = "사용자 인증 성공";
    String AUTH_ME_FAIL = "존재하지 않는 아이디이거나 비밀번호가 올바르지 않습니다.";
    String AUTH_FAIL_LACK_AUTH = "권한이 부족합니다. 관리자에게 문의하세요.";
    String AUTH_FAIL_NOT_AUTHENTICATED = "인증되지 않은 요청입니다. 로그인 후 다시 시도해주세요.";
}
