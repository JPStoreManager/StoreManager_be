package manage.store.model.common.value;

import manage.store.exception.common.InvalidParameterException;

import java.util.Objects;

public class DeleteFlag {
    public static final DeleteFlag YES = new DeleteFlag("Y");
    public static final DeleteFlag NO = new DeleteFlag("N");

    private final String value;

    private DeleteFlag(String value) {
        this.value = value;
    }

    public static DeleteFlag from(String value) {
        if ("Y".equalsIgnoreCase(value)) {
            return YES;
        }
        if ("N".equalsIgnoreCase(value)) {
            return NO;
        }
        throw new InvalidParameterException("유효하지 않은 삭제 플래그 값입니다: " + value);
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteFlag that = (DeleteFlag) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}