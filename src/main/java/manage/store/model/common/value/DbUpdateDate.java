package manage.store.model.common.value;

import manage.store.exception.common.InvalidParameterException;

import java.time.LocalDateTime;

public class DbUpdateDate {

    private LocalDateTime dateTime;

    public DbUpdateDate(LocalDateTime dateTime) {
        if(dateTime == null) {
            throw new InvalidParameterException("Date cannot be null.");
        }

        this.dateTime = dateTime;
    }

    public LocalDateTime value() {
        return dateTime;
    }

    public DbUpdateDate setDate(LocalDateTime dateTime) {
        return new DbUpdateDate(dateTime);
    }

    @Override
    public String toString() {
        return dateTime.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DbUpdateDate that = (DbUpdateDate) o;

        return dateTime.equals(that.dateTime);
    }

}
