package manage.store.testUtils.user;

import manage.store.model.user.user.User;
import org.assertj.core.api.Assertions;

public class UserUtils {

    public static void assertUser(User expected, User actual) {
        Assertions.assertThat(expected).isNotNull();
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(expected.getId()).isEqualTo(actual.getId());
        Assertions.assertThat(expected.getPassword()).isEqualTo(actual.getPassword());
        Assertions.assertThat(expected.getName()).isEqualTo(actual.getName());
        Assertions.assertThat(expected.getResidentRegistNo()).isEqualTo(actual.getResidentRegistNo());
        Assertions.assertThat(expected.getPhoneNo()).isEqualTo(actual.getPhoneNo());
        Assertions.assertThat(expected.getEmail()).isEqualTo(actual.getEmail());
        Assertions.assertThat(expected.getAddress()).isEqualTo(actual.getAddress());
        Assertions.assertThat(expected.getAuthCd()).isEqualTo(actual.getAuthCd());
        Assertions.assertThat(expected.getWorkStartDate()).isEqualTo(actual.getWorkStartDate());
        Assertions.assertThat(expected.getWorkEndDate()).isEqualTo(actual.getWorkEndDate());
        Assertions.assertThat(expected.getWorkStatusCd()).isEqualTo(actual.getWorkStatusCd());
        Assertions.assertThat(expected.getBankName()).isEqualTo(actual.getBankName());
        Assertions.assertThat(expected.getBankAccountNo()).isEqualTo(actual.getBankAccountNo());
        Assertions.assertThat(expected.getMonthSalary()).isEqualTo(actual.getMonthSalary());
        Assertions.assertThat(expected.getHourWage()).isEqualTo(actual.getHourWage());
        Assertions.assertThat(expected.getOtpNo()).isEqualTo(actual.getOtpNo());
        Assertions.assertThat(expected.getDeleteFlag()).isEqualTo(actual.getDeleteFlag());
        Assertions.assertThat(expected.getCreatedBy()).isEqualTo(actual.getCreatedBy());
        Assertions.assertThat(expected.getLastUpdatedBy()).isEqualTo(actual.getLastUpdatedBy());
    }
}
