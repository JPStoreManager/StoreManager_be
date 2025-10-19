package manage.store.model.common.value;

import com.fasterxml.jackson.annotation.JsonValue;
import manage.store.exception.common.InvalidParameterException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class RegistDate {
    private static final String REGIST_DATE_PATTERN = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";

    private final String value;

    public RegistDate(String value) {
        if (!isValidRegistDate(value))
            throw new InvalidParameterException("Registration date must be in the format YYYY-MM-DD. Provided: " + value);

        this.value = value;
    }

    public RegistDate(int year, int month, int day) {
        String formattedRegistDateStr = String.format("%04d-%02d-%02d", year, month, day);
        if(!isValidRegistDate(formattedRegistDateStr))
            throw new InvalidParameterException("Registration date must be in the format YYYY-MM-DD. Provided: " + formattedRegistDateStr);

        this.value = formattedRegistDateStr;
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

        RegistDate that = (RegistDate) o;

        return this.value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    private boolean isValidRegistDate(String value) {
        if (value == null || !value.matches(REGIST_DATE_PATTERN)) return false;
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            dateFormat.parse(value);

            return true;
        } catch (ParseException e){
            return false;
        }
    }
}
