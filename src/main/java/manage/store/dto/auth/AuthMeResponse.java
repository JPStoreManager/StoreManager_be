package manage.store.dto.auth;

import lombok.Getter;
import lombok.Setter;
import manage.store.dto.common.BaseResponse;
import manage.store.model.common.value.SuccessFlag;

@Getter
@Setter
public class AuthMeResponse extends BaseResponse {
    private String userId;

    public AuthMeResponse(SuccessFlag successFlag, String msg, String userId) {
        super(successFlag, msg);
        this.userId = userId;
    }

}
