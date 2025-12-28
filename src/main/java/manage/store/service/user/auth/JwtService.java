package manage.store.service.user.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import manage.store.consts.Env;
import manage.store.exception.common.auth.InvalidTokenException;
import manage.store.model.user.userAuth.LoginUserJwtClaim;
import manage.store.model.user.value.UserId;
import manage.store.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;

@Slf4j
@Service
public class JwtService {

    private final String ISSUER = "JPStoreManager";

    private final String CUSTOM_CLAIM_KEY = "data";

    private final String SECRET_KEY;

    private final long VALID_SEC;

    @Autowired
    public JwtService(Environment environment) {
        SECRET_KEY = environment.getProperty(Env.SECRET_JWT_SECRET_KEY.getPath());
        VALID_SEC = Long.valueOf(environment.getProperty(Env.SECRET_JWT_VALID_SEC.getPath()));
    }

    // 토큰 생성
    public String create(UserId userId) {
        if(userId == null) throw new InvalidTokenException("userId must not be empty");

        Header header = createHeader();
        SecretKey secretKey = getSecretKey(SECRET_KEY);

        String jws = Jwts.builder()
                // header
                .header().add(header)
                .and()
                // payload
                .claims().add(CUSTOM_CLAIM_KEY, createClaim(userId.value()))
                .expiration(getExpirationDate(VALID_SEC))
                .issuer(ISSUER)
                .and()
                // 대칭키를 활용한 서명
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();

        return jws;
    }

    /**
     * jwt 토큰이 유효한지 검증합니다.
     * 유효한 토큰이라면 Payload에 포함되어 있던 사용자 정보를 반환합니다.
     * @param jwt jwt 토큰 문자열
     * @return 사용자 정보가 들어있는 객체
     */
    public LoginUserJwtClaim verify(String jwt) {
        SecretKey secretKey = getSecretKey(SECRET_KEY);

        // 1) 서명 검증
        Jws<Claims> parsedJws = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwt);

        // 2) 만료시간 & 발행자 등 Registered Claim 검증
        // 2-1) 발행자 검증
        Claims payload = parsedJws.getPayload();
        if(payload == null) throw new InvalidTokenException("payload가 없는 토큰입니다.");

        String issuer = payload.getIssuer();
        if(!ISSUER.equals(issuer)) throw new InvalidTokenException("발행자가 잘못된 토큰입니다.");

        // 2-2) 만료 시간 검증
        Date expirationDate = payload.getExpiration();
        Date now = DateTimeUtils.convertToDate(DateTimeUtils.nowDateTime());

        // 현재가 만료시간보다 지난 경우 -> 만료된 토큰인 경우
        if(now.after(expirationDate)) throw new InvalidTokenException("만료기한이 지난 토큰입니다.");

        // 3) 실제 데이터 반환
        try {
            LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) payload.get(CUSTOM_CLAIM_KEY);

            return new LoginUserJwtClaim((String) data.get("userId"));
        } catch (ClassCastException | NullPointerException e) {
            throw new InvalidTokenException("Custom Claim 형식이 잘못된 토큰입니다.");
        }
    }

    /**
     * JWT Header 반환
     * @return Jwt Header
     */
    private Header createHeader() {
        return Jwts.header().type("jwt").build();
    }

    /**
     * Jwt Payload에 들어갈 Custom Claim 반환
     * @param userId 사용자 아이디
     * @return custom claim 객체
     */
    private LoginUserJwtClaim createClaim(String userId) {
        return new LoginUserJwtClaim(userId);
    }

    /**
     * Jwt 만료 시간 반환
     * @return 만료 날짜
     */
    private Date getExpirationDate(final long validSec) {
        LocalDateTime now = DateTimeUtils.nowDateTime();
        LocalDateTime expireDate = now.plusSeconds(validSec);

        return DateTimeUtils.convertToDate(expireDate);
    }


    /**
     * 가공된 비밀키를 반환합니다.
     * @param secretKey 문자열로된 비밀키
     * @return 가공되어 복잡해진 비밀키
     */
    private SecretKey getSecretKey(final String secretKey) {
        Base64.Encoder encoder = Base64.getEncoder();

        String encodedOnceSecretKey = new String(encoder.encode(secretKey.getBytes(StandardCharsets.UTF_8)));
        // Secret Key를 길게 늘리기 위함
        StringBuilder sb = new StringBuilder(encodedOnceSecretKey);
        for (int i = 0; i < 5; i++) {
            sb.append(encodedOnceSecretKey);
        }

        SecretKey result = Keys.hmacShaKeyFor(sb.toString().getBytes(StandardCharsets.UTF_8));

        return result;
    }

}
