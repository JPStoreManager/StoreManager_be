package manage.store.model.common.value;

import manage.store.exception.common.InvalidParameterException;

public class CommonCodeGrpCd {

    private static final int MAX_LENGTH = 20;

    public static final String UI_FORMAT = "UI_FORMAT";

    private String value;

    public CommonCodeGrpCd(String grpCd) {
        if(!isValidGrpCd(grpCd)) throw new InvalidParameterException("GrpCd is not valid. grpCd: " + grpCd);

        this.value = grpCd;
    }

    public String value() {
        return value;
    }

    public CommonCodeGrpCd setGrpCd(String grpCd) {
        return new CommonCodeGrpCd(grpCd);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommonCodeGrpCd that = (CommonCodeGrpCd) o;

        return this.value.equals(that.value);
    }

    private boolean isValidGrpCd(String grpCd) {
        return grpCd != null && !grpCd.isEmpty() && grpCd.length() <= MAX_LENGTH;
    }

}
