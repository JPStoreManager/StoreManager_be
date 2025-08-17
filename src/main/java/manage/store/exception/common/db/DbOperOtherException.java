package manage.store.exception.common.db;

public class DbOperOtherException extends DatabaseOperationException {
    public DbOperOtherException(String message) {
        super(message);
    }

    public DbOperOtherException(String message, Throwable cause) {
        super(message, cause);
    }
}
