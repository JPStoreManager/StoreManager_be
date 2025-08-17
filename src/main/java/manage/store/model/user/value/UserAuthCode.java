package manage.store.model.user.value;

import manage.store.exception.common.InvalidParameterException;

public class UserAuthCode {
    // DB에서 가변적으로 데이터를 변경하기 위해 오픈형 생성자로 관리중
    public static final UserAuthCode ROLE_ADMIN = new UserAuthCode("ROLE_ADMIN");
    public static final UserAuthCode ROLE_OWNER = new UserAuthCode("ROLE_OWNER");
    public static final UserAuthCode ROLE_MANAGER = new UserAuthCode("ROLE_MANAGER");
    public static final UserAuthCode ROLE_PART_TIMER = new UserAuthCode("ROLE_PART_TIMER");

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
