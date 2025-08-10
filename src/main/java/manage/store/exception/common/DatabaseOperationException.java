package manage.store.exception.common;

public class DatabaseOperationException extends RuntimeException {
    public DatabaseOperationException(String message) {
        super(message);
    }

    public DatabaseOperationException() {
        super("Database operation failed");
    }

    public DatabaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
