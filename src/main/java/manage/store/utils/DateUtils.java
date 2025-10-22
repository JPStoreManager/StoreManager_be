package manage.store.utils;

import manage.store.exception.common.InvalidParameterException;
import org.springframework.util.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;

public class DateUtils {

    /**
     * 주어진 연도와 월에 해당하는 날짜의 개수를 반환합니다.
     * @param year 년도 (1 이상)
     * @param month 월 (1부터 12까지)
     * @return 해당 월의 날짜 수
     * @throws InvalidParameterException 만약 연도가 1보다 작거나, 월이 1보다 작거나 12보다 큰 경우 예외를 발생시킵니다.
     */
    public static int getDaysCntInMonth(Integer year, Integer month) {
        if (!(isYearValid(year) && isMonthValid(month))) {
            throw new InvalidParameterException("Month must be between 1 and 12");
        }

        return YearMonth.of(year, month).lengthOfMonth();
    }

    /**
     * 주어진 날짜 문자열에 해당하는 요일을 반환합니다.
     * @param date 날짜 문자열 yyyy-MM-dd (예: "2023-10-01")
     * @return DayOfWeek 해당 날짜의 요일 enum
     */
    public static DayOfWeek getDayOfWeek(String date) {
        if (!StringUtils.hasText(date)) {
            throw new InvalidParameterException("Date string cannot be null or empty");
        }

        try {
            LocalDate localDate = LocalDate.parse(date);

            return localDate.getDayOfWeek();
        }catch (DateTimeParseException e) {
            throw new InvalidParameterException("Invalid date format. Please use 'yyyy-MM-dd'. parameter: " + date);
        }
    }

    /**
     * 연도가 유효한지 확인합니다.
     * @param year 연도
     * @return true - 년도가 1 이상일 경우, false - 그외 경우
     */
    public static boolean isYearValid(Integer year) {
        return year != null && year > 0;
    }

    /**
     * 월이 유효한지 확인합니다.
     * @param month 월
     * @return true - 월이 1 이상 12 이하일 경우, false - 그외 경우
     */
    public static boolean isMonthValid(Integer month) {
        return month != null && 1 <= month && month <= 12;
    }
}
