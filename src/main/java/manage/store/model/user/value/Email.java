package manage.store.model.user.value;

import manage.store.exception.common.InvalidParameterException;

import java.util.regex.Pattern;

public class Email {

    private static final String EMAIL_FORMAT = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_FORMAT);

    private String email;

    public Email(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidParameterException("Invalid email format");
        }
        this.email = email;
    }

    public String value() {
        return email;
    }

    public Email setEmail(String email) {
        return new Email(email);
    }

    @Override
    public String toString() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email1 = (Email) o;
        return email.equals(email1.email);
    }
}
