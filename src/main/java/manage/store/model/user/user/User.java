package manage.store.model.user.user;


import lombok.Getter;
import lombok.ToString;
import manage.store.model.common.value.DbUpdateDate;
import manage.store.model.common.value.DeleteFlag;
import manage.store.model.user.value.*;

import java.util.Objects;


@Getter
@ToString
public class User {

    private UserId id;
    private String password;
    private UserName name;
    private ResidentRegistNo residentRegistNo;
    private PhoneNo phoneNo;
    private Email email;
    private String address;
    private UserAuthCode authCd;
    private WorkDate workStartDate;
    private WorkDate workEndDate;
    private WorkStatusCode workStatusCd;
    private String bankName;
    private String bankAccountNo;
    private Salary monthSalary;
    private Salary hourWage;
    private OtpNo otpNo;
    private DeleteFlag deleteFlag;
    private UserId createdBy;
    private DbUpdateDate createdDate;
    private UserId lastUpdatedBy;
    private DbUpdateDate lastUpdatedDate;

    /** Setters **/
    public User setId(UserId id) {
        this.id = id;
        return new User(this);
    }

    public User setPassword(String password) {
        this.password = password;
        return new User(this);
    }

    public User setName(UserName name) {
        this.name = name;
        return new User(this);
    }

    public User setResidentRegistNo(ResidentRegistNo residentRegistNo) {
        this.residentRegistNo = residentRegistNo;
        return new User(this);
    }

    public User setPhoneNo(PhoneNo phoneNo) {
        this.phoneNo = phoneNo;
        return new User(this);
    }

    public User setEmail(Email email) {
        this.email = email;
        return new User(this);
    }

    public User setAddress(String address) {
        this.address = address;
        return new User(this);
    }

    public User setAuthCd(UserAuthCode authCd) {
        this.authCd = authCd;
        return new User(this);
    }

    public User setWorkStartDate(WorkDate workStartDate) {
        this.workStartDate = workStartDate;
        return new User(this);
    }

    public User setWorkEndDate(WorkDate workEndDate) {
        this.workEndDate = workEndDate;
        return new User(this);
    }

    public User setWorkStatusCd(WorkStatusCode workStatusCd) {
        this.workStatusCd = workStatusCd;
        return new User(this);
    }

    public User setBankName(String bankName) {
        this.bankName = bankName;
        return new User(this);
    }

    public User setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
        return new User(this);
    }

    public User setMonthSalary(Salary monthSalary) {
        this.monthSalary = monthSalary;
        return new User(this);
    }

    public User setHourWage(Salary hourWage) {
        this.hourWage = hourWage;
        return new User(this);
    }

    public User setOtpNo(OtpNo otpNo) {
        this.otpNo = otpNo;
        return new User(this);
    }

    public User setDeleteFlag(DeleteFlag deleteFlag) {
        this.deleteFlag = deleteFlag;
        return new User(this);
    }

    public User setCreatedBy(UserId createdBy) {
        this.createdBy = createdBy;
        return new User(this);
    }

    public User setLastUpdatedBy(UserId lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return new User(this);
    }

    /** Validation **/
    // 각 프로퍼티별 validation을 검증하는 메서드를 작성해
    private boolean isUserIdValid(UserId id) {
        return id != null;
    }

    private boolean isPasswordValid(String password) {
        final int MAX_PASSWORD_LENGTH = 300;

        return password != null && !password.isBlank() && password.length() <= MAX_PASSWORD_LENGTH;
    }

    private boolean isNameValid(UserName name) {
        return name != null;
    }

    private boolean isResidentRegistNoValid(ResidentRegistNo residentRegistNo) {
        return true;
    }

    private boolean isPhoneNoValid(PhoneNo phoneNo) {
        return phoneNo != null;
    }

    private boolean isEmailValid(Email email) {
        return true;
    }

    private boolean isAddressValid(String address) {
        final int MAX_ADDRESS_LENGTH = 200;

        return address == null || address.length() <= MAX_ADDRESS_LENGTH;
    }

    private boolean isAuthCdValid(UserAuthCode authCd) {
        return authCd != null;
    }

    private boolean isWorkStartDateValid(WorkDate workStartDate) {
        return workStartDate != null;
    }

    private boolean isWorkEndDateValid(WorkDate workEndDate) {
        return true;
    }

    private boolean isWorkStatusCdValid(WorkStatusCode workStatusCd) {
        return workStatusCd != null;
    }

    private boolean isBankNameValid(String bankName) {
        final int MAX_BANK_NAME_LENGTH = 6;

        return bankName == null || bankName.length() <= MAX_BANK_NAME_LENGTH;
    }

    private boolean isBankAccountNoValid(String bankAccountNo) {
        final int MAX_BANK_ACCOUNT_NO_LENGTH = 20;

        return bankAccountNo == null || bankAccountNo.length() <= MAX_BANK_ACCOUNT_NO_LENGTH;
    }

    private boolean isMonthSalaryValid(Salary monthSalary) {
        return true;
    }

    private boolean isHourWageValid(Salary hourWage) {
        return true;
    }

    private boolean isOtpNoValid(OtpNo otpNo) {
        return true;
    }

    private boolean isCreatedByValid(UserId createdBy) {
        return createdBy != null;
    }

    private boolean isLastUpdatedByValid(UserId lastUpdatedBy) {
        return lastUpdatedBy != null;
    }

    /**
     * 현재 객체의 유효한 데이터를 가지고 있는 객체인지 검사
     * @return true: 유효한 객체, false: 유효하지 않은 객체
     */
    public boolean isValid() {
        return isUserIdValid(getId()) &&
                isPasswordValid(getPassword()) &&
                isNameValid(getName()) &&
                isResidentRegistNoValid(getResidentRegistNo()) &&
                isPhoneNoValid(getPhoneNo()) &&
                isEmailValid(getEmail()) &&
                isAddressValid(getAddress()) &&
                isAuthCdValid(getAuthCd()) &&
                isWorkStartDateValid(getWorkStartDate()) &&
                isWorkEndDateValid(getWorkEndDate()) &&
                isWorkStatusCdValid(getWorkStatusCd()) &&
                isBankNameValid(getBankName()) &&
                isBankAccountNoValid(getBankAccountNo()) &&
                isMonthSalaryValid(getMonthSalary()) &&
                isHourWageValid(getHourWage()) &&
                isOtpNoValid(getOtpNo()) &&
                isCreatedByValid(getCreatedBy()) &&
                isLastUpdatedByValid(getLastUpdatedBy());
    }

    private User(User user) {
        this.id = user.id;
        this.password = user.password;
        this.name = user.name;
        this.residentRegistNo = user.residentRegistNo;
        this.phoneNo = user.phoneNo;
        this.email = user.email;
        this.address = user.address;
        this.authCd = user.authCd;
        this.workStartDate = user.workStartDate;
        this.workEndDate = user.workEndDate;
        this.workStatusCd = user.workStatusCd;
        this.bankName = user.bankName;
        this.bankAccountNo = user.bankAccountNo;
        this.monthSalary = user.monthSalary;
        this.hourWage = user.hourWage;
        this.otpNo = user.otpNo;
        this.deleteFlag = user.deleteFlag;
        this.createdBy = user.createdBy;
        this.createdDate = user.createdDate;
        this.lastUpdatedBy = user.lastUpdatedBy;
        this.lastUpdatedDate = user.lastUpdatedDate;

        if (!isValid()) {
            throw new IllegalArgumentException("Invalid User object.");
        }
    }

    public User(UserId id, String password, UserName name, ResidentRegistNo residentRegistNo,
                PhoneNo phoneNo, Email email, String address, UserAuthCode authCd,
                WorkDate workStartDate, WorkDate workEndDate, WorkStatusCode workStatusCd,
                String bankName, String bankAccountNo, Salary monthSalary, Salary hourWage,
                OtpNo otpNo, DeleteFlag deleteFlag, UserId createdBy, DbUpdateDate createdDate,
                UserId lastUpdatedBy, DbUpdateDate lastUpdatedDate) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.residentRegistNo = residentRegistNo;
        this.phoneNo = phoneNo;
        this.email = email;
        this.address = address;
        this.authCd = authCd;
        this.workStartDate = workStartDate;
        this.workEndDate = workEndDate;
        this.workStatusCd = workStatusCd;
        this.bankName = bankName;
        this.bankAccountNo = bankAccountNo;
        this.monthSalary = monthSalary;
        this.hourWage = hourWage;
        this.otpNo = otpNo;
        this.deleteFlag = deleteFlag;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdatedDate = lastUpdatedDate;

        if (!isValid()) {
            throw new IllegalArgumentException("Invalid User object.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId());
    }

    /**
     * 현재 객체가 삭제된 사용자 객체인지 검사
     * @return true: 삭제된 사용자, false: 삭제되지 않은 사용자
     */
    public boolean isDeleted() {
        return DeleteFlag.YES.equals(getDeleteFlag());
    }
}


