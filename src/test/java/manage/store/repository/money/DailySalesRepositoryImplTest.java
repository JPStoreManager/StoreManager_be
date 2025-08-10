package manage.store.repository.money;

import manage.store.consts.Tags;
import manage.store.exception.common.DatabaseOperationException;
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
import org.springframework.dao.DataIntegrityViolationException;

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
    @DisplayName("insertSales 실패 - DB 예외")
    void insertSales_fail_dbException() {
        final DailySales sales = SalesUtils.createSales("B001", "2024-06-01", new UserId("admin"));
        given(salesMapper.insert(sales)).willThrow(new DataIntegrityViolationException("DB Error"));
        assertThrows(DatabaseOperationException.class, () -> salesRepository.insertSales(sales));
    }

    @Test
    @DisplayName("insertSales 실패 - 업데이트 카운트가 0 또는 2 이상")
    void insertSales_fail_invalidUpdateCount() {
        final DailySales sales = SalesUtils.createSales("B001", "2024-06-01", new UserId("admin"));
        given(salesMapper.insert(sales)).willReturn(0);
        assertThrows(DatabaseOperationException.class, () -> salesRepository.insertSales(sales));

        given(salesMapper.insert(sales)).willReturn(2);
        assertThrows(DatabaseOperationException.class, () -> salesRepository.insertSales(sales));
    }


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

    @Test
    @DisplayName("updateSales 실패 - DB 예외")
    void updateSales_fail_dbException() {
        final DailySales sales = SalesUtils.createSales("B001", "2024-06-01", new UserId("admin"));
        given(salesMapper.update(sales)).willThrow(new DataIntegrityViolationException("DB Error"));
        assertThrows(DatabaseOperationException.class, () -> salesRepository.updateSales(sales));
    }

    @Test
    @DisplayName("updateSales 실패 - 업데이트 카운트가 0 또는 2 이상")
    void updateSales_fail_invalidUpdateCount() {
        final DailySales sales = SalesUtils.createSales("B001", "2024-06-01", new UserId("admin"));
        given(salesMapper.update(sales)).willReturn(0);
        assertThrows(DatabaseOperationException.class, () -> salesRepository.updateSales(sales));

        given(salesMapper.update(sales)).willReturn(2);
        assertThrows(DatabaseOperationException.class, () -> salesRepository.updateSales(sales));
    }
}

