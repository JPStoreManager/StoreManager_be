package manage.store.dto.common;

import lombok.Getter;
import lombok.ToString;
import manage.store.exception.common.InvalidParameterException;
import manage.store.model.common.value.SuccessFlag;
import org.springframework.util.StringUtils;

@Getter
@ToString
public class BaseResult {

    protected final SuccessFlag result;
    protected final String msg;

    private BaseResult(SuccessFlag result, String msg) {
        if(result == null || (!result.isSuccess() && !StringUtils.hasText(msg)))
            throw new InvalidParameterException("The parameter is invalid." + result + ", " + msg);

        this.result = result;
        this.msg = msg;
    }

    /**
     * 처리 성공 응답 생성
     * @return BaseResult
     */
    public static BaseResult success(String msg) {
        return new BaseResult(SuccessFlag.Y, msg);
    }

    /**
     * 처리 성공 응답 생성
     * @return BaseResult
     */
    public static BaseResult success() {
        return success(null);
    }

    /**
     * 처리 실패 응답 생성
     * @param msg 오류가 발생한 이유에 대한 메세지
     * @return BaseResult
     */
    public static BaseResult fail(String msg) {
        return new BaseResult(SuccessFlag.N, msg);
    }

    /**
     * 처리 결과의 성공 여부 반환
     * @return 성공 시 true, 실패 시 false
     */
    public boolean isSuccess() {
        return this.result.isSuccess();
    }

}
