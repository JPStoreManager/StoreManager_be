package manage.store.model.money.sales.value;

public class Money {

    private Long amount;

    public Money(Long amount) {
        if (amount == null || amount < 0) throw new IllegalArgumentException("Amount must be a non-negative value.");

        this.amount = amount;
    }

    public Long value() {
        return amount;
    }

    public Money add(Money money) {
        if (money == null) throw new IllegalArgumentException("Money to add cannot be null.");

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
