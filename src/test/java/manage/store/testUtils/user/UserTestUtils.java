package manage.store.testUtils.user;

import manage.store.model.common.value.DbUpdateDate;
import manage.store.model.common.value.DeleteFlag;
import manage.store.model.user.user.User;
import manage.store.model.user.userAuth.UserAuth;
import manage.store.model.user.value.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface UserTestUtils {

    LocalDateTime DUMMY_DATE = LocalDateTime.of(2025, 7, 13, 0, 0, 0);

    User DUMMY_USER1 = new User(
            new UserId("dummy1"),
            "$2a$10$KFuPNo6WTWJ/mbtHP4FThegk/AEyMe9.OrQzGBW5cbIpmFYeQSmEW",
            new UserName("test1"),
            new ResidentRegistNo("1111111111113"),
            new PhoneNo("12345678910"),
            new Email("dummy1@dummy1.com"),
            "address1",
            new UserAuthCode("ROLE_ADMIN"),
            new WorkDate(LocalDate.of(1990, 1, 1)),
            null,
            new WorkStatusCode("W"),
            "system",
            "system",
            null,
            null,
            null,
            DeleteFlag.NO,
            new UserId("system"),
            new DbUpdateDate(DUMMY_DATE),
            new UserId("system"),
            new DbUpdateDate(DUMMY_DATE)
    );

    User DUMMY_USER2 = new User(
            new UserId("dummy2"),
            "$2a$10$KFuPNo6WTWJ/mbtHP4FThegk/AEyMe9.OrQzGBW5cbIpmFYeQSmEW",
            new UserName("test2"),
            new ResidentRegistNo("1111111111114"),
            new PhoneNo("12345678911"),
            new Email("dummy2@dummy2.com"),
            "address2",
            new UserAuthCode("ROLE_OWNER"),
            new WorkDate(LocalDate.of(1990, 1, 2)),
            null,
            new WorkStatusCode("W"),
            "system",
            "system",
            null,
            null,
            null,
            DeleteFlag.NO,
            new UserId("system"),
            new DbUpdateDate(DUMMY_DATE),
            new UserId("system"),
            new DbUpdateDate(DUMMY_DATE)
    );

    User DUMMY_USER3 = new User(
            new UserId("dummy3"),
            "$2a$10$KFuPNo6WTWJ/mbtHP4FThegk/AEyMe9.OrQzGBW5cbIpmFYeQSmEW",
            new UserName("test3"),
            new ResidentRegistNo("1111111111115"),
            new PhoneNo("12345678912"),
            new Email("dummy3@dummy3.com"),
            "address3",
            new UserAuthCode("ROLE_PART_TIMER"),
            new WorkDate(LocalDate.of(1990, 1, 3)),
            null,
            new WorkStatusCode("W"),
            "system",
            "system",
            null,
            null,
            null,
            DeleteFlag.NO,
            new UserId("system"),
            new DbUpdateDate(DUMMY_DATE),
            new UserId("system"),
            new DbUpdateDate(DUMMY_DATE)
    );

    User DUMMY_USER4 = new User(
            new UserId("dummy4"),
            "$2a$10$KFuPNo6WTWJ/mbtHP4FThegk/AEyMe9.OrQzGBW5cbIpmFYeQSmEW",
            new UserName("test4"),
            new ResidentRegistNo("1111111111116"),
            new PhoneNo("12345678913"),
            new Email("dummy4@dummy4.com"),
            "address4",
            new UserAuthCode("ROLE_MANAGER"),
            new WorkDate(LocalDate.of(1990, 1, 4)),
            null,
            new WorkStatusCode("W"),
            "system",
            "system",
            null,
            null,
            null,
            DeleteFlag.YES,
            new UserId("system"),
            new DbUpdateDate(DUMMY_DATE),
            new UserId("system"),
            new DbUpdateDate(DUMMY_DATE)
    );

    User DUMMY_USER5 = new User(
            new UserId("dummy5"),
            "$2a$10$KFuPNo6WTWJ/mbtHP4FThegk/AEyMe9.OrQzGBW5cbIpmFYeQSmEW",
            new UserName("test5"),
            new ResidentRegistNo("1111111111117"),
            new PhoneNo("12345678914"),
            new Email("dummy5@dummy5.com"),
            "address5",
            new UserAuthCode("ROLE_PART_TIMER"),
            new WorkDate(LocalDate.of(1990, 1, 5)),
            null,
            new WorkStatusCode("W"),
            "system",
            "system",
            null,
            null,
            null,
            DeleteFlag.YES,
            new UserId("system"),
            new DbUpdateDate(DUMMY_DATE),
            new UserId("system"),
            new DbUpdateDate(DUMMY_DATE)
    );

    User[] USERS = {DUMMY_USER1, DUMMY_USER2, DUMMY_USER3, DUMMY_USER4, DUMMY_USER5};
}