package manage.store.controller.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import manage.store.dto.common.BaseResponse;
import manage.store.model.common.value.SuccessFlag;
import manage.store.utils.ExceptionUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ErrorHandlerController {

    @ExceptionHandler(Exception.class)
    public BaseResponse handleException(Exception e) {
        log.error("[결함 오류 발생] {}", ExceptionUtils.getExceptionErrorMsg(e));

        return new BaseResponse(SuccessFlag.N, "시스템 오류가 발생하였습니다. 관리자에게 연락해주세요.");
    }

}
