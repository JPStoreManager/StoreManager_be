package manage.store.model.common.value;

import com.fasterxml.jackson.annotation.JsonValue;
import manage.store.exception.common.InvalidParameterException;

import java.time.LocalDate;
import java.time.temporal.WeekFields;

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

        // ISO 기준: 한 주의 시작을 월요일로 간주
        // WeekFields.of(Locale.KOREA): 한 주의 시작을 일요일로 간주
        int weekNumber = date.get(WeekFields.ISO.weekOfMonth());
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
    
}