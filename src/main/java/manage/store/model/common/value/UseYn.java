package manage.store.model.common.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UseYn {

    Y("Y"),
    N("N")
    ;

    private final String code;

    public static UseYn fromCode(String code) {
        for (UseYn useYn : UseYn.values()) {
            if (useYn.getCode().equals(code)) {
                return useYn;
            }
        }
        throw new IllegalArgumentException("Invalid UseYn code: " + code);
    }
}
