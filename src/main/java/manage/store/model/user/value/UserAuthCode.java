package manage.store.model.user.value;

import manage.store.exception.common.InvalidParameterException;

public class UserAuthCode {

    private final String authCd;

    public UserAuthCode(String authCd) {
        if(authCd == null || authCd.isEmpty() || authCd.length() > 20) throw new InvalidParameterException("authCd is null or empty");

        this.authCd = authCd;
    }

    public String value() {
        return authCd;
    }

    public UserAuthCode setAuthCd(String authCd) {
        return new UserAuthCode(authCd);
    }

    @Override
    public String toString() {
        return authCd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAuthCode that = (UserAuthCode) o;

        return authCd.equals(that.authCd);
    }
}
