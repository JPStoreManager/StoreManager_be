package manage.store.model.common.value;

import com.fasterxml.jackson.annotation.JsonValue;
import manage.store.exception.common.InvalidParameterException;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class WeekNumber {

    private static final int MIN_WEEK_NUMBER = 1;
    private static final int MAX_WEEK_NUMBER = 6;

    private int value;

    public WeekNumber(int value) {
        if(!isValid(value)) throw new InvalidParameterException(getErrorMsg(value));

        this.value = value;
    }

    public WeekNumber(LocalDate date) {
        if(date == null) throw new InvalidParameterException("Date cannot be null when determining week number.");

        int weekNumber = getWeekNumberAtMonth(date);
        if(!isValid(weekNumber)) throw new InvalidParameterException(getErrorMsg(value));

        this.value = weekNumber;
    }

    @JsonValue
    public int value() {
        return value;
    }

    public WeekNumber setValue(int value) {
        return new WeekNumber(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeekNumber that = (WeekNumber) o;

        return value == that.value;
    }

    private boolean isValid(int weekNumber) {
        return MIN_WEEK_NUMBER <= weekNumber && weekNumber <= MAX_WEEK_NUMBER;
    }

    private String getErrorMsg(int weekNumber) {
        return "Invalid week number: " + weekNumber + ". It must be between " + MIN_WEEK_NUMBER + " and " + MAX_WEEK_NUMBER + ".";
    }

    /**
     * 날짜가 월의 몇 번쨰 주차에 헤당하는지 반환
     * @param date 주차를 알아낼 지정한 날짜
     * @return 주차
     */
    private int getWeekNumberAtMonth(LocalDate date) {
        int lengthOfMonth = date.lengthOfMonth();
        int year = date.getYear(), month = date.getMonthValue();
        DayOfWeek dayOfWeekAtFirstDayOfMonth = LocalDate.of(year, month, 1).getDayOfWeek();

        int weekNumber = dayOfWeekAtFirstDayOfMonth == DayOfWeek.MONDAY ? 0 : 1;
        for (int day = 1; day <= lengthOfMonth; day++) {
            LocalDate curDate = LocalDate.of(year, month, day);
            DayOfWeek curDayOfWeek = curDate.getDayOfWeek();
            // 월요일이면 새로운 주의 시작임으로 하나 증가
            if(curDayOfWeek == DayOfWeek.MONDAY) weekNumber++;

            if(day == date.getDayOfMonth()) break;
        }

        return (weekNumber);
    }
    
}