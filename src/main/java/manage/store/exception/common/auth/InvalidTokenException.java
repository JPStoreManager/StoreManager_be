package manage.store.exception.common.auth;

/**
 * 권한 인증 정보를 담고 있는 JWT가 유효하지 못한 경우 발생하는 익셉션
 */
public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }

}
