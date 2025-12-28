package manage.store.model.money.sales.value;

import com.fasterxml.jackson.annotation.JsonValue;
import manage.store.exception.common.InvalidParameterException;

public class Money {

    private final Long amount;

    public Money(Long amount) {
        if (amount == null || amount < 0) throw new InvalidParameterException("Amount must be a non-negative value.");

        this.amount = amount;
    }

    @JsonValue
    public Long value() {
        return amount;
    }

    public Money add(Money money) {
        if (money == null) throw new InvalidParameterException("Money to add cannot be null.");

        return new Money(this.amount + money.amount);
    }

    public Money setAmount(Long amount) {
        return new Money(amount);
    }

    @Override
    public String toString() {
        return String.valueOf(amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Money money = (Money) o;

        return amount.equals(money.amount);
    }

}
