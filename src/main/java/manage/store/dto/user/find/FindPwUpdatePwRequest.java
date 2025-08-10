package manage.store.dto.user.find;

import lombok.Getter;
import lombok.Setter;
import manage.store.config.annotation.user.NewPassword;

@Getter
@Setter
public class FindPwUpdatePwRequest extends FindPwBaseRequest{

    @NewPassword
    private String newPassword;

    public FindPwUpdatePwRequest() {
        super();
    }

}