package manage.store.model.user.value;

import manage.store.model.money.sales.value.Money;

public class Salary extends Money {

    public Salary(Long amount) {
        super(amount);
    }

    @Override
    public String toString() {
        return String.valueOf(value());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Salary salary = (Salary) o;

        return this.value().equals(salary.value());
    }

}
