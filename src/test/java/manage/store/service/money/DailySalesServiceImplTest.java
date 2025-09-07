package manage.store.service.money;

import manage.store.consts.Tags;
import manage.store.dto.money.month.GetMonthSalesRequest;
import manage.store.dto.money.month.GetMonthSalesResponse;
import manage.store.model.common.value.RegistDate;
import manage.store.model.money.sales.DailySales.DailySales;
import manage.store.model.money.sales.value.Money;
import manage.store.model.user.value.UserId;
import manage.store.repository.money.SalesRepository;
import manage.store.testUtils.money.SalesUtils;
import manage.store.utils.DateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@Tag(Tags.Test.UNIT)
@ExtendWith(MockitoExtension.class)
public class DailySalesServiceImplTest {

    @Mock
    private SalesRepository salesRepository;

    @InjectMocks
    private SalesServiceImpl salesService;

    @Test
    @DisplayName("getMonthSales 성공_nonEmpty")
    public void getMonthSalesTest_success() {
        // Given
        final String branchCd = "branch1";
        final int year = 2023, month = 10;
        final int sampleSize = 15;

        final List<DailySales> sampleSales = getSampleSales(branchCd, year, month).stream()
                .peek(s -> {
                    int tmpMoney = Integer.parseInt(s.getRegistDate().value().substring(8, 10));
                    s.setCashSales(new Money((long) tmpMoney));
                    s.setCardSales(new Money((long) tmpMoney));
                })
                .filter(sales -> sales.getRegistDate().value().compareTo(String.format("%04d-%02d-%d", year, month, sampleSize)) <= 0)
                .collect(Collectors.toList());

        given(salesRepository.selectSalesByMonth(branchCd, year, month)).willReturn(sampleSales);

        // When
        final GetMonthSalesRequest request = new GetMonthSalesRequest(branchCd, year, month);
        final List<GetMonthSalesResponse.DailySales> response = salesService.getMonthSales(request);

        // Then
        assertNotNull(response);
        // TODO 매입 기능 추가 시 매입에 대한 데이터 추가 필요
        // cardSales, cashSales, totalSales, cardPercentage, weeklyTotalSales, monthTotalSales
        final int[][] expectedSales = {
                {1, 1, 2, 50, 2, 2},
                {2, 2, 4, 50, 4, 6},
                {3, 3, 6, 50, 10, 12},
                {4, 4, 8, 50, 18, 20},
                {5, 5, 10, 50, 28, 30},
                {6, 6, 12, 50, 40, 42},
                {7, 7, 14, 50, 54, 56},
                {8, 8, 16, 50, 70, 72},
                {9, 9, 18, 50, 18, 90},
                {10, 10, 20, 50, 38, 110},
                {11, 11, 22, 50, 60, 132},
                {12, 12, 24, 50, 84, 156},
                {13, 13, 26, 50, 110, 182},
                {14, 14, 28, 50, 138, 210},
                {15, 15, 30, 50, 168, 240}
        };

        int day = 1;
        for (GetMonthSalesResponse.DailySales actual : response) {
            final GetMonthSalesResponse.DailySales expected = new GetMonthSalesResponse.DailySales();
            expected.setBranchCd(branchCd);
            expected.setRegistDate(new RegistDate(year, month, day));
            if(day <= sampleSize) {
                expected.setCardSales(new Money((long) expectedSales[day - 1][0]));
                expected.setCashSales(new Money((long) expectedSales[day - 1][1]));
                expected.setTotalSales(new Money((long) expectedSales[day - 1][2]));
                expected.setCardPercentage(expectedSales[day - 1][3]);
                expected.setWeeklyTotalSales(new Money((long) expectedSales[day - 1][4]));
                expected.setMonthTotalSales(new Money((long) expectedSales[day - 1][5]));
            } else {
                expected.setMonthTotalSales(new Money((long) expectedSales[expectedSales.length - 1][5]));
            }
            day++;
            // TODO 매입 기능 추가 시 주석 해제

            validateSales(expected, actual);
        }


    }

    @Test
    @DisplayName("getMonthSales 성공_empty")
    public void getMonthSalesTest_success_empty() {
        // Given
        final String branchCd = "branch1";
        final int year = 2023, month = 10;
        
        given(salesRepository.selectSalesByMonth(branchCd, year, month)).willReturn(new ArrayList<>());

        // When
        final GetMonthSalesRequest request = new GetMonthSalesRequest(branchCd, year, month);
        final List<GetMonthSalesResponse.DailySales> result = salesService.getMonthSales(request);

        // Then
        assertNotNull(result);
        final GetMonthSalesResponse.DailySales expected = new GetMonthSalesResponse.DailySales();
        expected.setBranchCd(branchCd);
        expected.setCardSales(new Money(0L));
        expected.setCashSales(new Money(0L));
        expected.setTotalSales(new Money(0L));
        expected.setCardPercentage(0);
        expected.setWeeklyTotalSales(new Money(0L));
        expected.setMonthTotalSales(new Money(0L));

        int day = 1;
        for (GetMonthSalesResponse.DailySales res : result) {
            expected.setRegistDate(new RegistDate(year, month, day++));
            validateSales(expected, res);
        }

    }

    @Test
    @DisplayName("getMonthSales 실패_invalidParameters")
    public void getMonthSalesTest_fail_invalidParameters() {
        final GetMonthSalesRequest invalidReq1 = new GetMonthSalesRequest(null, 2023, 10);
        assertThrows(IllegalArgumentException.class, () -> salesService.getMonthSales(invalidReq1));

        final GetMonthSalesRequest invalidReq2 = new GetMonthSalesRequest("", 2023, 13);
        assertThrows(IllegalArgumentException.class, () -> salesService.getMonthSales(invalidReq2));

        final GetMonthSalesRequest invalidReq3 = new GetMonthSalesRequest("branch1", 0, 1);
        assertThrows(IllegalArgumentException.class, () -> salesService.getMonthSales(invalidReq3));

        final GetMonthSalesRequest invalidReq4 = new GetMonthSalesRequest("branch1", 2023, 0);
        assertThrows(IllegalArgumentException.class, () -> salesService.getMonthSales(invalidReq4));

        final GetMonthSalesRequest invalidReq5 = new GetMonthSalesRequest("branch1", 2023, 13);
        assertThrows(IllegalArgumentException.class, () -> salesService.getMonthSales(invalidReq5));
    }

    private List<DailySales> getSampleSales(String branchCd, int year, int month) {
        final int daysCntInMonth = DateUtils.getDaysCntInMonth(year, month);
        final List<DailySales> data = new ArrayList<>(daysCntInMonth);
        for (int i = 0; i < daysCntInMonth; i++) {
            String date = String.format("%04d-%02d-%02d", year, month, i + 1);

            DailySales sales = SalesUtils.createSales(branchCd, date, new UserId("tester"));
            data.add(sales);
        }
        
        return data;
    }
    
    private void validateSales(GetMonthSalesResponse.DailySales expected, GetMonthSalesResponse.DailySales actual) {
        assertNotNull(actual);
        Assertions.assertEquals(expected.getBranchCd(), actual.getBranchCd());
        Assertions.assertEquals(expected.getRegistDate(), actual.getRegistDate());
        Assertions.assertEquals(expected.getCardSales(), actual.getCardSales());
        Assertions.assertEquals(expected.getCashSales(), actual.getCashSales());
        Assertions.assertEquals(expected.getTotalSales(), actual.getTotalSales());
        Assertions.assertEquals(expected.getCardPercentage(), actual.getCardPercentage());
        Assertions.assertEquals(expected.getWeeklyTotalSales(), actual.getWeeklyTotalSales());
        Assertions.assertEquals(expected.getMonthTotalSales(), actual.getMonthTotalSales());
        // TODO 매입 기능 추가 시 주석 해제
//        Assertions.assertEquals(expected.getExpense(), actual.getExpense());
//        Assertions.assertEquals(expected.getMonthTotalExpense(), actual.getMonthTotalExpense());
//        Assertions.assertEquals(expected.getVariableExpense(), actual.getVariableExpense());
        Assertions.assertEquals(expected.getComment(), actual.getComment());
    }
    
    
}