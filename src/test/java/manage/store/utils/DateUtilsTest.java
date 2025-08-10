package manage.store.utils;

import manage.store.consts.Tags;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.*;

@Tag(Tags.Test.UNIT)
class DateUtilsTest {


    @Test
    @DisplayName("getDaysCntInMonth 성공")
    void getDaysCntInMonth_success() {
        // Given
        final int year = 2023, month = 3;
        final int expectedDays = 31;

        // When
        final int actualDays = DateUtils.getDaysCntInMonth(year, month);

        // Then
        assertEquals(expectedDays, actualDays);
    }

    @Test
    @DisplayName("getDaysCntInMonth 실패 - 잘못된 연도")
    void getDaysCntInMonth_fail_invalidYear() {
        // Given
        final int year = 0, month = 2;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> DateUtils.getDaysCntInMonth(year, month));
    }

    @Test
    @DisplayName("getDaysCntInMonth 실패 - 잘못된 월")
    void getDaysCntInMonth_fail_invalidMonth() {
        // Given
        final int year = 2023, invalidMonth1 = 13, invalidMonth2 = 0; // 잘못된 월

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> DateUtils.getDaysCntInMonth(year, invalidMonth1));
        assertThrows(IllegalArgumentException.class, () -> DateUtils.getDaysCntInMonth(year, invalidMonth2));
    }

    @Test
    @DisplayName("getDayOfWeek 성공")
    void getDayOfWeek_success() {
        // Given
        final String date = "2023-10-01"; // 일요일

        // When
        final DayOfWeek dayOfWeek = DateUtils.getDayOfWeek(date);

        // Then
        assertEquals(DayOfWeek.SUNDAY, dayOfWeek);
    }

    @Test
    @DisplayName("getDayOfWeek 실패 - 잘못된 날짜 형식")
    void getDayOfWeek_fail_invalidDateFormat() {
        // Given
        final String[] invalidDateString = new String[]{
                "2023-10-32",
                "2023-13-01",
                "2023-10-01T00:00:00",
                "2023/10/01",
                "hello world",
        };

        // When & Then
        for (String invalidDateStr : invalidDateString) {
            assertThrows(IllegalArgumentException.class, () -> DateUtils.getDayOfWeek(invalidDateStr));
        }
    }

    @Test
    @DisplayName("isYearValid 성공")
    void isYearValid_success() {
        // Given
        final int validYear = 2023;

        // When
        final boolean isValid = DateUtils.isYearValid(validYear);

        // Then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("isYearValid 실패 - 잘못된 연도")
    void isYearValid_fail_invalidYear() {
        // Given
        final Integer invalidYear1 = 0, invalidYear2 = null; // 잘못된 연도

        // When
        final boolean isValid1 = DateUtils.isYearValid(invalidYear1);
        final boolean isValid2 = DateUtils.isYearValid(invalidYear2);

        // Then
        assertFalse(isValid1);
        assertFalse(isValid2);
    }

    @Test
    @DisplayName("isMonthValid 성공")
    void isMonthValid_success() {
        // Given
        final int validMonth = 5; // 5월

        // When
        final boolean isValid = DateUtils.isMonthValid(validMonth);

        // Then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("isMonthValid 실패 - 잘못된 월")
    void isMonthValid_fail_invalidMonth() {
        // Given
        final Integer[] invalidMonthes = new Integer[]{
                0, 13, null // 잘못된 월
        };

        // When
        for (Integer invalidMonth : invalidMonthes) {
            assertFalse(DateUtils.isMonthValid(invalidMonth));
        }
    }


}