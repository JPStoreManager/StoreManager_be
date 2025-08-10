package manage.store.model.common.value;

public class SortOrder {

    private Long order;

    public SortOrder(Long sortOrder) {
        if (!isValidSortOrder(sortOrder))
            throw new IllegalArgumentException("Sort order must be a non-negative integer. Provided: " + sortOrder);

        this.order = sortOrder;
    }

    public Long value() {
        return order;
    }

    public SortOrder setOrder(Long sortOrder) {
        return new SortOrder(sortOrder);
    }

    @Override
    public String toString() {
        return String.valueOf(order);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SortOrder sortOrder = (SortOrder) o;

        return this.order.equals(sortOrder.order);
    }

    private boolean isValidSortOrder(Long sortOrder) {
        return sortOrder != null && sortOrder >= 0;
    }

}
