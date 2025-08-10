package manage.store.model.common.value;

import manage.store.exception.common.InvalidParameterException;

public class CommonCodeCd {

    private static final int MAX_LENGTH = 30;

    public static final String UI_FORMAT = "UI_FORMAT";

    private String value;

    public CommonCodeCd(String cd) {
        if(!isValidCd(cd)) throw new InvalidParameterException("Cd is not valid. cd: " + cd);

        this.value = cd;
    }

    public String value() {
        return value;
    }

    public CommonCodeCd setValue(String value) {
        return new CommonCodeCd(value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommonCodeCd that = (CommonCodeCd) o;

        return this.value.equals(that.value);
    }

    private boolean isValidCd(String cd) {
        return cd != null && !cd.isEmpty() && cd.length() <= MAX_LENGTH;
    }

}
