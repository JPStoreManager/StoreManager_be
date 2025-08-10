package manage.store.model.user.value;

public class UserId {

    private static final int MAX_USER_ID_LENGTH = 100;

    private String id;

    public UserId(String userId) {
        if(userId == null || userId.length() > MAX_USER_ID_LENGTH)
            throw new IllegalArgumentException("User ID must be a non-empty string with a maximum length of " + MAX_USER_ID_LENGTH + " characters.");

        this.id = userId;
    }

    public String value() {
        return id;
    }

    public UserId setId(String id) {
        return new UserId(id);
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserId userId = (UserId) o;

        return id.equals(userId.id);
    }

}
