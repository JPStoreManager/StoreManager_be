package manage.store.consts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 *  application.yml 파일에 있는 환경 변수들의 path를 담고 있는 enum
 */
@ToString
@Getter
@RequiredArgsConstructor
public enum Env {

    SECRET_JWT_SECRET_KEY("secret.jwt.secret-key"),
    SECRET_JWT_VALID_SEC("secret.jwt.valid-sec")
    ;

    private final String path;
}
