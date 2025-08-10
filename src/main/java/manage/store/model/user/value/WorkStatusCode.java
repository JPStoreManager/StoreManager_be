package manage.store.model.user.value;

public class WorkStatusCode {

    private static final int MAX_LENGTH = 20;

    private String code;

    public WorkStatusCode(String code) {
        if (code == null || code.isEmpty() || code.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Work status code cannot be null or empty and must not exceed " + MAX_LENGTH + " characters.");
        }

        this.code = code;
    }

    public String value() {
        return code;
    }

    public WorkStatusCode setCode(String code) {
        return new WorkStatusCode(code);
    }

    @Override
    public String toString() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkStatusCode that = (WorkStatusCode) o;

        return code.equals(that.code);
    }
}
