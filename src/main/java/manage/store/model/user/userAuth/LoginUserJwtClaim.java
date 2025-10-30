package manage.store.model.user.userAuth;

import manage.store.exception.common.InvalidParameterException;
import org.springframework.util.StringUtils;

public record LoginUserJwtClaim(String userId) {

    public LoginUserJwtClaim {
        if (!StringUtils.hasText(userId)) {
            throw new InvalidParameterException("userId must not be empty");
        }

    }

}

