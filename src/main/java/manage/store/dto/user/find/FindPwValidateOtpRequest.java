package manage.store.dto.user.find;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import manage.store.dto.user.find.FindPwBaseRequest;

@Getter
@Setter
public class FindPwValidateOtpRequest extends FindPwBaseRequest {

    @NotBlank
    private String otp;

    public FindPwValidateOtpRequest() {
        super();
    }

}