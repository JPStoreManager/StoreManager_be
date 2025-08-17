package manage.store.exception.common.db;

public class DbOperNonTransientException extends RuntimeException {
    public DbOperNonTransientException(String message) {
        super(message);
    }

    public DbOperNonTransientException(String message, Throwable cause) {
        super(message, cause);
    }
}
