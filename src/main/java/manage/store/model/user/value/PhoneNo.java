package manage.store.model.user.value;

public class PhoneNo {

    private static final int LENGTH = 11;

    private String phoneNo;

    public PhoneNo(String phoneNo) {
        if (phoneNo == null || phoneNo.length() != LENGTH) {
            throw new IllegalArgumentException("Phone number is null, empty, or exceeds maximum length of 15 characters.");
        }
        this.phoneNo = phoneNo;
    }

    public String value() {
        return phoneNo;
    }

    public PhoneNo setPhoneNo(String phoneNo) {
        return new PhoneNo(phoneNo);
    }

    @Override
    public String toString() {
        return phoneNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNo phoneNo1 = (PhoneNo) o;
        return phoneNo.equals(phoneNo1.phoneNo);
    }

}
