package manage.store.dto.auth;

import lombok.Getter;
import lombok.Setter;
import manage.store.dto.common.BaseResponse;
import manage.store.model.common.value.SuccessFlag;
import manage.store.model.user.value.UserId;

@Getter
@Setter
public class AuthMeResponse extends BaseResponse {
    private UserId userId;

    public AuthMeResponse(SuccessFlag successFlag, String msg, UserId userId) {
        super(successFlag, msg);
        this.userId = userId;
    }

}
