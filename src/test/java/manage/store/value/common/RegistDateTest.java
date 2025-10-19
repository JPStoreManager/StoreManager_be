package manage.store.value.common;

import manage.store.consts.Const;
import manage.store.consts.Tags;
import manage.store.exception.common.InvalidParameterException;
import manage.store.model.common.value.RegistDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag(Tags.Test.UNIT)
public class RegistDateTest {

    @Test
    @DisplayName("RegistDate 생성 성공")
    void create_success() {
        // when
        final String[] validDates = {
                "2023-01-01",
                "2023-12-31",
                "2000-02-29",
                "2024-02-29",
                "1999-12-31"
        };

        for (String validDate : validDates) {
            RegistDate registDate = new RegistDate(validDate);
            // then
            assertThat(registDate).isNotNull();
            assertThat(registDate.value()).isEqualTo(validDate);
        }
    }

    @Test
    @DisplayName("RegistDate 생성 실패 - 잘못된 형식")
    void create_fail_invalidFormat() {
        // when
        final String[] invalidDates = {
                "2023/01/01",
                "01-01-2023",
                "2023-1-1",
                "2023-13-01",
                "2023-00-10",
                "2023-01-32",
                "2023-02-29",
                "abcd-ef-gh",
                "",
                "20230101",
                "2023-001-01",
                "2023-01-001",
                "2023-1-01",
                "2023-01-1",
                null
        };
        for (String invalidDate : invalidDates) {
            Assertions.assertThrows(InvalidParameterException.class, () -> {new RegistDate(invalidDate);});
        }
    }
}
