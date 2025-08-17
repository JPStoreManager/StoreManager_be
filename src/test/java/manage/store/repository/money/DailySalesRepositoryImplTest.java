package manage.store.repository.money;

import manage.store.consts.Tags;
import manage.store.exception.common.db.*;
import manage.store.exception.common.InvalidParameterException;
import manage.store.model.money.sales.DailySales.DailySales;
import manage.store.model.user.value.UserId;
import manage.store.repository.money.mapper.SalesMapper;
import manage.store.testUtils.money.SalesUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.jdbc.BadSqlGrammarException;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@Tag(Tags.Test.UNIT)
@ExtendWith(MockitoExtension.class)
class DailySalesRepositoryImplTest {

    @Mock
    private SalesMapper salesMapper;

    @InjectMocks
    private SalesRepositoryImpl salesRepository;

    /** selectSalesByYear */
    @Test
    @DisplayName("selectSalesByYear 성공")
    void selectSalesByYear_success() {
        // Given
        final String branchCd = "B001";
        final int year = 2024;
        final List<DailySales> salesList = Collections.singletonList(SalesUtils.createSales(branchCd, "2024-01-01", new UserId("admin")));
        given(salesMapper.selectByYear(branchCd, year)).willReturn(salesList);

        // When
        final List<DailySales> result = salesRepository.selectSalesByYear(branchCd, year);

        // Then
        assertThat(result.size()).isEqualTo(salesList.size());
    }

    @Test
    @DisplayName("selectSalesByYear 실패 - 잘못된 파라미터")
    void selectSalesByYear_fail_invalidParameter() {
        assertThrows(InvalidParameterException.class, () -> salesRepository.selectSalesByYear(null, 2024));
        assertThrows(InvalidParameterException.class, () -> salesRepository.selectSalesByYear("", 2024));
        assertThrows(InvalidParameterException.class, () -> salesRepository.selectSalesByYear("  ", 2025));

        assertThrows(InvalidParameterException.class, () -> salesRepository.selectSalesByYear("B001", null));
        assertThrows(InvalidParameterException.class, () -> salesRepository.selectSalesByYear("B001", 0));

        assertThrows(InvalidParameterException.class, () -> salesRepository.selectSalesByYear(null, null));
    }

    @Test
    @DisplayName("selectSalesByYear 실패 - DB 조회 중 Non-transient 예외 발생")
    void selectSalesByYear_fail_dbNonTransientException() {
        // Given
        final String branchCd = "B001";
        final int year = 2024;
        given(salesMapper.selectByYear(branchCd, year))
                .willThrow(new BadSqlGrammarException("test", "invalid sql", new SQLException()));

        // When & Then
        assertThrows(DbOperNonTransientException.class, () -> salesRepository.selectSalesByYear(branchCd, year));
    }

    @Test
    @DisplayName("selectSalesByYear 실패 - DB 조회 중 Transient 예외 발생")
    void selectSalesByYear_fail_dbTransientException() {
        // Given
        final String branchCd = "B001";
        final int year = 2024;
        given(salesMapper.selectByYear(branchCd, year)).willThrow(new QueryTimeoutException("DB Error"));

        // When & Then
        assertThrows(DbOperTransientException.class, () -> salesRepository.selectSalesByYear(branchCd, year));
    }

    @Test
    @DisplayName("selectSalesByYear 실패 - DB 조회 중 일반 DataAccess 예외 발생")
    void selectSalesByYear_fail_dbDataAccessException() {
        // Given
        final String branchCd = "B001";
        final int year = 2024;
        given(salesMapper.selectByYear(branchCd, year))
                .willThrow(new DataAccessException("General DB Error"){});

        // When & Then
        assertThrows(DbOperDataAccessException.class, () -> salesRepository.selectSalesByYear(branchCd, year));
    }

    @Test
    @DisplayName("selectSalesByYear 실패 - DB 조회 중 기타 예외 발생")
    void selectSalesByYear_fail_dbOtherException() {
        // Given
        final String branchCd = "B001";
        final int year = 2024;
        given(salesMapper.selectByYear(branchCd, year))
                .willThrow(new RuntimeException("Unexpected error"));

        // When & Then
        assertThrows(DbOperOtherException.class, () -> salesRepository.selectSalesByYear(branchCd, year));
    }

    /** selectSalesByMonth */
    @Test
    @DisplayName("selectSalesByMonth 성공")
    void selectSalesByMonth_success() {
        // Given
        final String branchCd = "B001";
        final int year = 2024, month = 5;
        final List<DailySales> salesList = Collections.singletonList(SalesUtils.createSales(branchCd, "2024-05-01", new UserId("admin")));
        given(salesMapper.selectByMonth(branchCd, year, month)).willReturn(salesList);

        // When
        final List<DailySales> result = salesRepository.selectSalesByMonth(branchCd, year, month);

        // Then
        assertThat(result.size()).isEqualTo(salesList.size());
    }

    @Test
    @DisplayName("selectSalesByMonth 실패 - 잘못된 파라미터")
    void selectSalesByMonth_fail_invalidParameter() {
        assertThrows(InvalidParameterException.class, () -> salesRepository.selectSalesByMonth(null, 2024, 5));
        assertThrows(InvalidParameterException.class, () -> salesRepository.selectSalesByMonth("", 2024, 5));
        assertThrows(InvalidParameterException.class, () -> salesRepository.selectSalesByMonth("  ", 2024, 5));

        assertThrows(InvalidParameterException.class, () -> salesRepository.selectSalesByMonth("B001", null, 5));
        assertThrows(InvalidParameterException.class, () -> salesRepository.selectSalesByMonth("B001", 0, 5));

        assertThrows(InvalidParameterException.class, () -> salesRepository.selectSalesByMonth("B001", 2024, null));
        assertThrows(InvalidParameterException.class, () -> salesRepository.selectSalesByMonth("B001", 2024, 0));
        assertThrows(InvalidParameterException.class, () -> salesRepository.selectSalesByMonth("B001", 2024, 13));
    }

    @Test
    @DisplayName("selectSalesByMonth 실패 - DB 조회 중 Non-transient 예외 발생")
    void selectSalesByMonth_fail_dbNonTransientException() {
        // Given
        final String branchCd = "B001";
        final int year = 2024, month = 5;
        given(salesMapper.selectByMonth(branchCd, year, month))
                .willThrow(new BadSqlGrammarException("test", "invalid sql", new SQLException()));

        // When & Then
        assertThrows(DbOperNonTransientException.class, () -> salesRepository.selectSalesByMonth(branchCd, year, month));
    }

    @Test
    @DisplayName("selectSalesByMonth 실패 - DB 조회 중 Transient 예외 발생 ")
    void selectSalesByMonth_fail_dbTransientException() {
        // Given
        final String branchCd = "B001";
        final int year = 2024, month = 5;
        given(salesMapper.selectByMonth(branchCd, year, month)).willThrow(new QueryTimeoutException("DB Error"));

        // When & Then
        assertThrows(DbOperTransientException.class, () -> salesRepository.selectSalesByMonth(branchCd, year, month));
    }

    @Test
    @DisplayName("selectSalesByMonth 실패 - DB 조회 중 일반 DataAccess 예외 발생")
    void selectSalesByMonth_fail_dbDataAccessException() {
        // Given
        final String branchCd = "B001";
        final int year = 2024, month = 5;
        given(salesMapper.selectByMonth(branchCd, year, month))
                .willThrow(new DataAccessException("General DB Error") {});

        // When & Then
        assertThrows(DbOperDataAccessException.class, () -> salesRepository.selectSalesByMonth(branchCd, year, month));
    }

    @Test
    @DisplayName("selectSalesByMonth 실패 - DB 조회 중 기타 예외 발생")
    void selectSalesByMonth_fail_dbOtherException() {
        // Given
        final String branchCd = "B001";
        final int year = 2024, month = 5;
        given(salesMapper.selectByMonth(branchCd, year, month))
                .willThrow(new RuntimeException("Unexpected error"));

        // When & Then
        assertThrows(DbOperOtherException.class, () -> salesRepository.selectSalesByMonth(branchCd, year, month));
    }

    /** insertSales */
    @Test
    @DisplayName("insertSales 성공")
    void insertSales_success() {
        // Given
        final DailySales sales = SalesUtils.createSales("B001", "2024-06-01", new UserId("admin"));
        given(salesMapper.insert(sales)).willReturn(1);

        // When
        final int updatedCnt = salesRepository.insertSales(sales);

        // Then
        assertThat(updatedCnt).isEqualTo(1);
    }

    @Test
    @DisplayName("insertSales 실패 - 잘못된 파라미터")
    void insertSales_fail_invalidParameter() {
        final DailySales validSales = SalesUtils.createSales("B001", "2024-06-01", new UserId("admin"));
        // null 전달
        assertThrows(InvalidParameterException.class, () -> salesRepository.insertSales(null));

        final DailySales s1 = SalesUtils.clone(validSales);
        s1.setBranchCd(null);
        assertThrows(InvalidParameterException.class, () -> salesRepository.insertSales(s1));

        final DailySales s2 = SalesUtils.clone(validSales);
        s2.setRegistDate(null);
        assertThrows(InvalidParameterException.class, () -> salesRepository.insertSales(s2));


        final DailySales s3 = SalesUtils.clone(validSales);
        s3.setCreatedBy(null);
        assertThrows(InvalidParameterException.class, () -> salesRepository.insertSales(s3));

        final DailySales s4 = SalesUtils.clone(validSales);
        s4.setLastUpdatedBy(null);
        assertThrows(InvalidParameterException.class, () -> salesRepository.insertSales(s4));
    }

    @Test
    @DisplayName("insertSales 실패 - DB 조회 중 Non-transient 예외 발생")
    void insertSales_fail_dbNonTransientException() {
        // Given
        final DailySales sales = SalesUtils.createSales("B001", "2024-06-01", new UserId("admin"));
        given(salesMapper.insert(sales))
                .willThrow(new BadSqlGrammarException("test", "invalid sql", new SQLException()));

        // When & Then
        assertThrows(DbOperNonTransientException.class, () -> salesRepository.insertSales(sales));
    }

    @Test
    @DisplayName("insertSales 실패 - DB 조회 중 Transient 예외 발생")
    void insertSales_fail_dbTransientException() {
        // Given
        final DailySales sales = SalesUtils.createSales("B001", "2024-06-01", new UserId("admin"));
        given(salesMapper.insert(sales)).willThrow(new QueryTimeoutException("DB Error"));

        // When & Then
        assertThrows(DbOperTransientException.class, () -> salesRepository.insertSales(sales));
    }

    @Test
    @DisplayName("insertSales 실패 - DB 조회 중 일반 DataAccess 예외 발생")
    void insertSales_fail_dbDataAccessException() {
        // Given
        final DailySales sales = SalesUtils.createSales("B001", "2024-06-01", new UserId("admin"));
        given(salesMapper.insert(sales))
                .willThrow(new DataAccessException("General DB Error") {});

        // When & Then
        assertThrows(DbOperDataAccessException.class, () -> salesRepository.insertSales(sales));
    }

    @Test
    @DisplayName("insertSales 실패 - DB 조회 중 기타 예외 발생")
    void insertSales_fail_dbOtherException() {
        // Given
        final DailySales sales = SalesUtils.createSales("B001", "2024-06-01", new UserId("admin"));
        given(salesMapper.insert(sales))
                .willThrow(new RuntimeException("Unexpected error"));

        // When & Then
        assertThrows(DbOperOtherException.class, () -> salesRepository.insertSales(sales));
    }

    /** updateSales */
    @Test
    @DisplayName("updateSales 성공")
    void updateSales_success() {
        // Given
        final DailySales sales = SalesUtils.createSales("B001", "2024-06-01", new UserId("admin"));
        given(salesMapper.update(sales)).willReturn(1);

        // When
        final int updatedCnt = salesRepository.updateSales(sales);

        // Then
        assertThat(updatedCnt).isEqualTo(1);
    }

    @Test
    @DisplayName("updateSales 실패 - 잘못된 파라미터")
    void updateSales_fail_invalidParameter() {
        final DailySales validSales = SalesUtils.createSales("B001", "2024-06-01", new UserId("admin"));
        // null 전달
        assertThrows(InvalidParameterException.class, () -> salesRepository.updateSales(null));

        final DailySales s1 = SalesUtils.clone(validSales);
        s1.setBranchCd(null);
        assertThrows(InvalidParameterException.class, () -> salesRepository.updateSales(s1));

        final DailySales s2 = SalesUtils.clone(validSales);
        s2.setRegistDate(null);
        assertThrows(InvalidParameterException.class, () -> salesRepository.updateSales(s2));

        final DailySales s3 = SalesUtils.clone(validSales);
        s3.setCreatedBy(null);
        assertThrows(InvalidParameterException.class, () -> salesRepository.updateSales(s3));

        final DailySales s4 = SalesUtils.clone(validSales);
        s4.setLastUpdatedBy(null);
        assertThrows(InvalidParameterException.class, () -> salesRepository.updateSales(s4));
    }

    /**
     * DB 조회 중 Non-transient 예외 발생
     * DB 조회 중 Transient 예외 발생
     * DB 조회 중 일반 DataAccess 예외 발생
     * DB 조회 중 기타 예외 발생
     * 에 대한 테스트 코드
     */
    @Test
    @DisplayName("updateSales 실패 - DB 조회 중 Non-transient 예외 발생")
    void updateSales_fail_dbNonTransientException() {
        // Given
        final DailySales sales = SalesUtils.createSales("B001", "2024-06-01", new UserId("admin"));
        given(salesMapper.update(sales))
                .willThrow(new BadSqlGrammarException("test", "invalid sql", new SQLException()));

        // When & Then
        assertThrows(DbOperNonTransientException.class, () -> salesRepository.updateSales(sales));
    }

    @Test
    @DisplayName("updateSales 실패 - DB 조회 중 Transient 예외 발생")
    void updateSales_fail_dbTransientException() {
        // Given
        final DailySales sales = SalesUtils.createSales("B001", "2024-06-01", new UserId("admin"));
        given(salesMapper.update(sales)).willThrow(new QueryTimeoutException("DB Error"));

        // When & Then
        assertThrows(DbOperTransientException.class, () -> salesRepository.updateSales(sales));
    }

    @Test
    @DisplayName("updateSales 실패 - DB 조회 중 일반 DataAccess 예외 발생")
    void updateSales_fail_dbDataAccessException() {
        // Given
        final DailySales sales = SalesUtils.createSales("B001", "2024-06-01", new UserId("admin"));
        given(salesMapper.update(sales))
                .willThrow(new DataAccessException("General DB Error") {});

        // When & Then
        assertThrows(DbOperDataAccessException.class, () -> salesRepository.updateSales(sales));
    }

    @Test
    @DisplayName("updateSales 실패 - DB 조회 중 기타 예외 발생")
    void updateSales_fail_dbOtherException() {
        // Given
        final DailySales sales = SalesUtils.createSales("B001", "2024-06-01", new UserId("admin"));
        given(salesMapper.update(sales))
                .willThrow(new RuntimeException("Unexpected error"));

        // When & Then
        assertThrows(DbOperOtherException.class, () -> salesRepository.updateSales(sales));
    }
}

