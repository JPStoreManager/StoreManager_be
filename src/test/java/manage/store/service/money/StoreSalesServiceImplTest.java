package manage.store.service.money;

import manage.store.consts.Tags;
import manage.store.dto.money.sales.month.controller.GetMonthSalesRequest;
import manage.store.dto.money.sales.month.controller.GetMonthSalesResponse;
import manage.store.dto.money.sales.month.service.*;
import manage.store.exception.common.InvalidParameterException;
import manage.store.model.common.value.RegistDate;
import manage.store.model.common.value.WeekNumber;
import manage.store.model.common.value.YearMonth;
import manage.store.model.money.sales.DailySales.StoreSales;
import manage.store.model.money.sales.value.Money;
import manage.store.repository.money.SalesRepository;
import manage.store.service.money.sales.SalesServiceImpl;
import manage.store.service.money.sales.SalesSummaryService;
import manage.store.testUtils.money.MonthlySalesUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Tag(Tags.Test.UNIT)
@ExtendWith(MockitoExtension.class)
public class StoreSalesServiceImplTest {

    @Mock
    private SalesRepository salesRepository;

    @Mock
    private SalesSummaryService salesSummaryService;

    @InjectMocks
    private SalesServiceImpl salesService;

    @Test
    @DisplayName("getMonthSales 성공_nonEmpty")
    public void getMonthSalesTest_success() {
        // Given
        final String branchCd = MonthlySalesUtils.BRANCH_CD;
        final int year = MonthlySalesUtils.YEAR_MONTH.getYear(), month = MonthlySalesUtils.YEAR_MONTH.getMonth();
        final GetMonthSalesRequest param = new GetMonthSalesRequest(branchCd, year, month);

        final List<StoreSales> sampleStoreSales = MonthlySalesUtils.STORE_SALES_2023_05;
        given(salesRepository.selectSalesByMonth(branchCd, year, month)).willReturn(sampleStoreSales);

        final List<SalesDailySummary> salesDailySummary = MonthlySalesUtils.EXPECTED_DAILY_SUMMARY;
        final List<SalesWeeklySummary> salesWeeklySummary = MonthlySalesUtils.EXPECTED_WEEKLY_SUMMARY;
        final GetMonthlySalesSummaryRslt summaryRslt = new GetMonthlySalesSummaryRslt(
                branchCd,
                new YearMonth(year, month),
                salesDailySummary,
                salesWeeklySummary,
                MonthlySalesUtils.MONTH_TOTAL_CARD,
                MonthlySalesUtils.MONTH_TOTAL_CASH,
                MonthlySalesUtils.MONTH_TOTAL_CARD.add(MonthlySalesUtils.MONTH_TOTAL_CASH)
        );
        given(salesSummaryService.getMonthSalesSummary(any())).willReturn(summaryRslt);



        // When
        final GetMonthlySalesParam request = new GetMonthlySalesParam(branchCd, new YearMonth(year, month));
        GetMonthSalesResponse result = salesService.getMonthSales(request);

        // Then
        assertNotNull(result);
        Assertions.assertThat(result.getBranchCd()).isEqualTo(branchCd);
        Assertions.assertThat(result.getYearMonth()).isEqualTo(MonthlySalesUtils.YEAR_MONTH);

        // 일별
        assertNotNull(result.getDailySales());
        Assertions.assertThat(result.getDailySales().size()).isEqualTo(salesDailySummary.size());
        for (int i = 0; i < result.getDailySales().size(); i++) {
            GetMonthSalesResponse.DailySales actual = result.getDailySales().get(i);
            SalesDailySummary expectedSummary = salesDailySummary.get(i);
            BasicDailySales expectedBasicSales = MonthlySalesUtils.BASIC_DAILY_SALES_2023_05.get(i);

            Assertions.assertThat(actual.getBranchCd()).isEqualTo(expectedBasicSales.getBranchCd());
            Assertions.assertThat(actual.getRegistDate()).isEqualTo(expectedBasicSales.getRegistDate());
            Assertions.assertThat(actual.getCardSales()).isEqualTo(expectedBasicSales.getCardSales());
            Assertions.assertThat(actual.getCashSales()).isEqualTo(expectedBasicSales.getCashSales());
            Assertions.assertThat(actual.getTotalSales()).isEqualTo(expectedBasicSales.getTotalSales());
            Assertions.assertThat(actual.getCardPercentage()).isEqualTo(expectedBasicSales.getCardPercentage());
            Assertions.assertThat(actual.getWeeklyTotalSales()).isEqualTo(expectedSummary.getWeeklyTotalSales());
            Assertions.assertThat(actual.getMonthTotalSales()).isEqualTo(expectedSummary.getMonthTotalSales());
            // TODO 지출 관련 테스트 코드 추가
        }

        // 주별
        assertNotNull(result.getWeeklySales());
        Assertions.assertThat(result.getWeeklySales().size()).isEqualTo(salesWeeklySummary.size());
        for (int i = 0; i < result.getWeeklySales().size(); i++) {
            GetMonthSalesResponse.WeeklySales actual = result.getWeeklySales().get(i);
            SalesWeeklySummary expected = salesWeeklySummary.get(i);

            Assertions.assertThat(actual.getBranchCd()).isEqualTo(expected.getBranchCd());
            Assertions.assertThat(actual.getYearMonth()).isEqualTo(expected.getYearMonth());
            Assertions.assertThat(actual.getWeekNumber()).isEqualTo(expected.getWeekNumber());
            Assertions.assertThat(actual.getSalesAvg()).isEqualTo(expected.getSalesAvg());
            Assertions.assertThat(actual.getExpectedTotalSales()).isEqualTo(expected.getExpectedTotalSales());

            // TODO 지출 관련 테스트 코드 추가
        }

        // 월 총
        Assertions.assertThat(result.getMonthTotalCard()).isEqualTo(MonthlySalesUtils.MONTH_TOTAL_CARD);
        Assertions.assertThat(result.getMonthTotalCash()).isEqualTo(MonthlySalesUtils.MONTH_TOTAL_CASH);
        Assertions.assertThat(result.getMonthTotalSales()).isEqualTo(MonthlySalesUtils.MONTH_TOTAL_CARD.add(MonthlySalesUtils.MONTH_TOTAL_CASH));

    }

    @Test
    @DisplayName("getMonthSales 성공_empty")
    public void getMonthSalesTest_success_empty() {
        // Given
        final String branchCd = "branch1";
        final int year = 2023, month = 10;

        final List<SalesDailySummary> sampleSalesSummary = new ArrayList<>() {{
            for (int day = 1; day <= 31; day++) {
                add(new SalesDailySummary(branchCd, new RegistDate(year, month, day)));
            }
        }};

        final List<SalesDailySummary> salesDailySummary = new ArrayList<>() {{
            for (int day = 1; day <= 31; day++) {
                add(new SalesDailySummary(branchCd, new RegistDate(year, month, day)));
            }
        }};

        final List<SalesWeeklySummary> salesWeeklySummary = new ArrayList<>() {{
            for (int i = 1; i <= 5; i++) {
                LocalDate day = LocalDate.of(year, month, i);
                add(new SalesWeeklySummary(branchCd, new YearMonth(year, month), new WeekNumber(day)));
            }
        }};

        given(salesRepository.selectSalesByMonth(branchCd, year, month)).willReturn(new ArrayList<>());
        GetMonthlySalesSummaryRslt summaryRslt = new GetMonthlySalesSummaryRslt(branchCd, new YearMonth(year, month), salesDailySummary, salesWeeklySummary, new Money(0L), new Money(0L), new Money(0L));
        given(salesSummaryService.getMonthSalesSummary(any())).willReturn(summaryRslt);

        // When
        final GetMonthlySalesParam request = new GetMonthlySalesParam(branchCd, new YearMonth(year, month));
        final GetMonthSalesResponse result = salesService.getMonthSales(request);

        // Then
        assertNotNull(result);
        Assertions.assertThat(result.getBranchCd()).isEqualTo(branchCd);
        Assertions.assertThat(result.getYearMonth()).isEqualTo(new YearMonth(year, month));

        // 일별
        Assertions.assertThat(result.getDailySales().size()).isEqualTo(sampleSalesSummary.size());
        for (GetMonthSalesResponse.DailySales actual : result.getDailySales()) {
            Assertions.assertThat(actual.getBranchCd()).isEqualTo(branchCd);
            Assertions.assertThat(actual.getCardSales()).isEqualTo(new Money(0L));
            Assertions.assertThat(actual.getCashSales()).isEqualTo(new Money(0L));
            Assertions.assertThat(actual.getTotalSales()).isEqualTo(new Money(0L));
            Assertions.assertThat(actual.getCardPercentage()).isEqualTo(0);
            Assertions.assertThat(actual.getWeeklyTotalSales()).isEqualTo(new Money(0L));
            Assertions.assertThat(actual.getMonthTotalSales()).isEqualTo(new Money(0L));
        }

        // 주별
        Assertions.assertThat(result.getWeeklySales().size()).isEqualTo(5);
        for (GetMonthSalesResponse.WeeklySales actual : result.getWeeklySales()) {
            Assertions.assertThat(actual.getBranchCd()).isEqualTo(branchCd);
            Assertions.assertThat(actual.getSalesAvg()).isEqualTo(new Money(0L));
            Assertions.assertThat(actual.getExpectedTotalSales()).isEqualTo(new Money(0L));
        }

        // 월별
        Assertions.assertThat(result.getMonthTotalCard()).isEqualTo(new Money(0L));
        Assertions.assertThat(result.getMonthTotalCash()).isEqualTo(new Money(0L));
        Assertions.assertThat(result.getMonthTotalSales()).isEqualTo(new Money(0L));

    }

    @Test
    @DisplayName("getMonthSales 실패_invalidParameters")
    public void getMonthSalesTest_fail_invalidParameters() {
        final GetMonthlySalesParam invalidReq = null;
        assertThrows(InvalidParameterException.class, () -> salesService.getMonthSales(invalidReq));

    }
    
    
}