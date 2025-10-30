package manage.store.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 날짜 + 시간에 대한 날짜 객체를 활용할 수 있는 Util
 */
public class DateTimeUtils {

    /**
     * 한국 시간으로 현재 날짜에 대한 LocalDateTime를 반환
     * @return LocalDateTime 한국 현재 날짜
     */
    public static LocalDateTime nowDateTime() {
        return LocalDateTime.now(ZoneId.of(DateUtils.TIME_ZONE_SEOUL));
    }

    /**
     * LocalDateTime을 한국시간에 맞게 Date 객체로 수정
     * @param dateTime 변경하려는 객체
     * @return 변경된 Date 객체
     */
    public static Date convertToDate(LocalDateTime dateTime) {
        Instant expireDateInstant = dateTime.atZone(ZoneId.of(DateUtils.TIME_ZONE_SEOUL)).toInstant();

        return Date.from(expireDateInstant);
    }


}
