package manage.store.model.user.value;

import java.time.LocalDate;

public class WorkDate {

    private LocalDate workStartDate;

    public WorkDate(LocalDate workStartDate) {
        if (workStartDate == null) {
            throw new IllegalArgumentException("Work start date cannot be null.");
        }

        this.workStartDate = workStartDate;
    }

    public LocalDate value() {
        return workStartDate;
    }

    public WorkDate setWorkStartDate(LocalDate workStartDate) {
        return new WorkDate(workStartDate);
    }

    @Override
    public String toString() {
        return workStartDate.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkDate workDate = (WorkDate) o;
        return workStartDate.equals(workDate.workStartDate);
    }

}
