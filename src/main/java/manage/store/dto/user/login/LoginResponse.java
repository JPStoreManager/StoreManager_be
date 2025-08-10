package manage.store.dto.user.login;

import manage.store.dto.common.BaseResponse;
import manage.store.consts.Message;
import manage.store.model.common.value.SuccessFlag;


public class LoginResponse extends BaseResponse {

    public LoginResponse(SuccessFlag isSuccess) {
        super(isSuccess, isSuccess == SuccessFlag.Y ? Message.LOGIN_SUCCESS : Message.LOGIN_FAIL_NOT_EXIST_USER);
    }

}
