package manage.store.controller;

import lombok.extern.slf4j.Slf4j;
import manage.store.dto.common.BaseResponse;
import manage.store.dto.common.ParameterValidationFailResponse;
import manage.store.utils.ExceptionUtils;
import manage.store.exception.common.InvalidParameterException;
import manage.store.consts.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
public class BaseController {

    /**
     * Api 주석
     * 설명
     * @param 프로퍼티 {@code 타입, 필수여부}: 설명 <br>
     * @return 프로퍼티 {@code 타입, 필수여부}: 설명 <br>
     * @throws 예외 {@code 타입}: 익셉션 발생 케이스
     */
    // 필수 여부: mandatory / optional

    /**
     * 각 API의 바디 파라미터 검증 실패 시 처리 로직
     */
    @ExceptionHandler({InvalidParameterException.class, MethodArgumentNotValidException.class, MissingRequestHeaderException.class})
    public ResponseEntity<BaseResponse> handleInvalidParameterException(Exception e) {
        log.info("Invalid Parameter was inputted. Error Message: {}", ExceptionUtils.getExceptionErrorMsg(e));

        return ResponseEntity.badRequest().body(new ParameterValidationFailResponse(Message.FIND_PW_FAIL_INVALID_PARAM_OR_ACCESS));
    }

}
