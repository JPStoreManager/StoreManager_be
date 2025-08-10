package manage.store.dto.user.find;


import manage.store.dto.common.BaseResponse;
import manage.store.model.common.value.SuccessFlag;

public class FindPwUpdatePwResponse extends BaseResponse {

    public FindPwUpdatePwResponse(SuccessFlag isSuccess, String msg) {
        super(isSuccess, msg);
    }

}
