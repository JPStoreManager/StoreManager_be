package manage.store.testUtils.user;


import manage.store.model.common.value.DeleteFlag;
import manage.store.model.user.user.User;
import manage.store.model.user.value.*;

import java.time.LocalDate;

/**
 * Test용 임의 데이터
 */
public class UserData {
//    private static final String email = "JumpingParkManager@gmail.com";

    public static User user1() {
        return new User(
                new UserId("testerId1"),
                "$2a$10$GSis.dob/tMyiVXui7UNDOI5hlcnvbL3ryW8kIaGT7D31FcKK2OI.",
                new UserName("테스트1"),
                new ResidentRegistNo("1231231231231"),
                new PhoneNo("11111111111"),
                new Email("JumpingParkManager1@gmail.com"),
                "test address 1",
                UserAuthCode.ROLE_ADMIN,
                new WorkDate(LocalDate.of(2024, 1, 1)),
                null,
                new WorkStatusCode("ING"),
                "BANK1",
                "1231231231",
                new Salary(2000000L),
                new Salary(0L),
                new OtpNo("otp1"),
                DeleteFlag.NO,
                new UserId("hs.lee0130"),
                null,
                new UserId("hs.lee0130"),
                null
        );
    }

    public static User user2() {
        return new User(
                new UserId("testerId2"),
                "$2a$10$qa90EbI.v1OxaE1cWc/pSu6HHkWPb6/LSwqo9aBx5mCnzJqwH115.",
                new UserName("테스트2"),
                new ResidentRegistNo("1234123412341"),
                new PhoneNo("22222222222"),
                new Email("JumpingParkManager2@gmail.com"),
                "test address 2",
                UserAuthCode.ROLE_OWNER,
                new WorkDate(LocalDate.of(2024, 2, 1)),
                new WorkDate(LocalDate.of(2024, 12, 31)),
                new WorkStatusCode("END"),
                "BANK2",
                "1234123412",
                new Salary(0L),
                new Salary(10000L),
                new OtpNo("otp2"),
                DeleteFlag.NO,
                new UserId("hs.lee0130"),
                null,
                new UserId("hs.lee0130"),
                null
        );
    }

    public static User user3() {
        return new User(
                new UserId("testerId3"),
                "$2a$10$rCi9o8yyOGm7KzO2Q7kVH.fQShgIeEOTEMs.Md06LUynpqEq5oUYO",
                new UserName("테스트3"),
                new ResidentRegistNo("1234512345123"),
                new PhoneNo("33333333333"),
                new Email("JumpingParkManager3@gmail.com"),
                "test address 3",
                UserAuthCode.ROLE_MANAGER,
                new WorkDate(LocalDate.of(2024, 3, 1)),
                null,
                new WorkStatusCode("ING"),
                "BANK3",
                "12345123451",
                new Salary(12300000L),
                new Salary(0L),
                new OtpNo("otp3"),
                DeleteFlag.NO,
                new UserId("hs.lee0130"),
                null,
                new UserId("hs.lee0130"),
                null
        );
    }

    public static User user4() {
        return new User(
                new UserId("testerId4"),
                "$2a$10$GSis.dob/tMyiVXui7UNDOI5hlcnvbL3ryW8kIaGT7D31FcKK2OI.",
                new UserName("테스트4"),
                new ResidentRegistNo("1234561234561"),
                new PhoneNo("44444444444"),
                new Email("JumpingParkManager4@gmail.com"),
                "test address 4",
                UserAuthCode.ROLE_ADMIN,
                new WorkDate(LocalDate.of(2024, 4, 1)),
                null,
                new WorkStatusCode("ING"),
                "BANK4",
                "12341234124",
                new Salary(3000000L),
                new Salary(0L),
                new OtpNo("otp4"),
                DeleteFlag.NO,
                new UserId("hs.lee0130"),
                null,
                new UserId("hs.lee0130"),
                null
        );
    }

    public static User user5() {
        return new User(
                new UserId("testerId5"),
                "$2a$10$GSis.dob/tMyiVXui7UNDOI5hlcnvbL3ryW8kIaGT7D31FcKK2OI.",
                new UserName("테스트5"),
                new ResidentRegistNo("1234567123456"),
                new PhoneNo("55555555555"),
                new Email("JumpingParkManager5@gmail.com"),
                "test address 5",
                UserAuthCode.ROLE_OWNER,
                new WorkDate(LocalDate.of(2024, 5, 1)),
                null,
                new WorkStatusCode("ING"),
                "BANK5",
                "12341234125",
                new Salary(0L),
                new Salary(15000L),
                new OtpNo("otp5"),
                DeleteFlag.NO,
                new UserId("hs.lee0130"),
                null,
                new UserId("hs.lee0130"),
                null
        );
    }
}