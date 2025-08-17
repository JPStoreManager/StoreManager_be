package manage.store.exception.common.db;

public class DbOperTransientException extends DatabaseOperationException {
  public DbOperTransientException(String message) {
    super(message);
  }

    public DbOperTransientException(String message, Throwable cause) {
        super(message, cause);
    }
}
