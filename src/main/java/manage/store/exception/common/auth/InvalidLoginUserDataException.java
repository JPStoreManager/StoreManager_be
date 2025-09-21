package manage.store.exception.common.auth;

import org.springframework.security.core.AuthenticationException;

public class InvalidLoginUserDataException extends AuthenticationException {

    public InvalidLoginUserDataException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvalidLoginUserDataException(String msg) {
        super(msg);
    }

}
