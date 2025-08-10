package manage.store.model.user.value;

public class ResidentRegistNo {
    private static final int LENGTH = 13;

    private String residentRegistNo;

    public ResidentRegistNo(String residentRegistNo) {
        if (residentRegistNo == null || residentRegistNo.length() != LENGTH) {
            throw new IllegalArgumentException("Resident registration number is null or does not have the correct length.");
        }

        this.residentRegistNo = residentRegistNo;
    }

    public String value() {
        return residentRegistNo;
    }

    public ResidentRegistNo setResidentRegistNo(String residentRegistNo) {
        return new ResidentRegistNo(residentRegistNo);
    }

    @Override
    public String toString() {
        return residentRegistNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResidentRegistNo that = (ResidentRegistNo) o;
        return residentRegistNo.equals(that.residentRegistNo);
    }

}
