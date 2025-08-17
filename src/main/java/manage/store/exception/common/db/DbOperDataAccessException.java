package manage.store.exception.common.db;

public class DbOperDataAccessException extends DatabaseOperationException {
    public DbOperDataAccessException(String message) {
        super(message);
    }

    public DbOperDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
