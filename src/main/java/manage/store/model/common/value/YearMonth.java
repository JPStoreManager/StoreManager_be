package manage.store.model.common.value;

import com.fasterxml.jackson.annotation.JsonValue;
import manage.store.exception.common.InvalidParameterException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

/**
 * YYYY-MM 형식의 날짜 문자열을 나타내는 클래스
 */
public class YearMonth {
    private static final String REGIST_DATE_PATTERN = "^\\d{4}\\-(0[1-9]|1[012])$";

    private final String value;

    /**
     * YYYY-MM 형식의 날짜 문자열로 초기화
     * @param value YYYY-MM 형식의 날짜 문자열
     */
    public YearMonth(String value) {
        if (!isValidYearMonth(value))
            throw new InvalidParameterException("YearMonth must be in the format YYYY-MM. Provided: " + value);

        this.value = value;
    }

    public YearMonth(int year, int month) {
        String formattedYearMonth = String.format("%04d-%02d", year, month);
        if(!isValidYearMonth(formattedYearMonth))
            throw new InvalidParameterException("YearMonth must be in the format YYYY-MM. Provided: " + formattedYearMonth);

        this.value = formattedYearMonth;
    }

    @JsonValue
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

        YearMonth that = (YearMonth) o;

        return this.value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * YearMonth 객체를 LocalDate 객체로 변환하여 반환
     * 날짜는 1일로 설정
     * @return LocalDate 객체
     */
    public LocalDate convertToLocalDate() {
        String[] comps = this.value.split("-");
        int year = Integer.parseInt(comps[0]);
        int month = Integer.parseInt(comps[1]);
        int day = 1;

        return LocalDate.of(year, month, day);
    }

    public int getYear() {
        return convertToLocalDate().getYear();
    }

    public int getMonth() {
        return convertToLocalDate().getMonthValue();
    }

    private boolean isValidYearMonth(String value) {
        if (value == null || !value.matches(REGIST_DATE_PATTERN)) return false;
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            dateFormat.setLenient(false); // 명확하게 YYYY-MM인지 확인
            dateFormat.parse(value);

            return true;
        } catch (ParseException e){
            return false;
        }
    }
}

