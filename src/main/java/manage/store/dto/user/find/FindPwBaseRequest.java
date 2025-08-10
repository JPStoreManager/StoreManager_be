package manage.store.dto.user.find;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import manage.store.config.annotation.user.UserEmail;

@NoArgsConstructor
@Getter
@Setter
public class FindPwBaseRequest {

    @NotBlank
    protected String userId;

    @UserEmail
    protected String email;

}

