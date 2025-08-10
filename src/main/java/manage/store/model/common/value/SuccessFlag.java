package manage.store.model.common.value;

import lombok.Getter;

@Getter
public enum SuccessFlag {
    Y("Y"),
    N("N");

    private final String value;

    SuccessFlag(String value) {
        this.value = value;
    }

    public boolean isSuccess() {
        return this == Y;
    }
}
