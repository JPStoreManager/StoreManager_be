package manage.store.model.user.value;

public class UserName {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 5;

    private String name;

    public UserName(String name) {
        if (name == null || name.length() < MIN_LENGTH || name.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("User name must be between " + MIN_LENGTH + " and " + MAX_LENGTH + " characters long.");
        }
        this.name = name;
    }

    public String value() {
        return name;
    }

    public UserName setName(String name) {
        return new UserName(name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserName userName = (UserName) o;

        return name.equals(userName.name);
    }
}
