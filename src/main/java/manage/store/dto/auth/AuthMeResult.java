package manage.store.dto.auth;

import lombok.Getter;
import lombok.Setter;
import manage.store.model.user.value.UserId;

@Getter
@Setter
public class AuthMeResult {

    private UserId userId;

    public AuthMeResult(UserId userId) {
        this.userId = userId;
    }

}
