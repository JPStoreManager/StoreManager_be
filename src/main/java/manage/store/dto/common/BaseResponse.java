package manage.store.dto.common;

import lombok.Getter;
import lombok.ToString;
import manage.store.exception.common.InvalidParameterException;
import manage.store.model.common.value.SuccessFlag;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Getter
@ToString
public class BaseResponse {
    protected final SuccessFlag result;
    protected final String msg;
    protected final LocalDateTime timestamp;

    public BaseResponse(SuccessFlag isSuccess, String msg) {
        if(isSuccess == null || !StringUtils.hasText(msg))
            throw new InvalidParameterException("The parameter is invalid." + isSuccess + ", " + msg);

        this.result = isSuccess;
        this.msg = msg;
        this.timestamp = LocalDateTime.now();
    }
}
