package manage.store.service.money.sales;

import manage.store.consts.Tags;
import manage.store.dto.money.sales.month.service.*;
import manage.store.exception.common.InvalidParameterException;
import manage.store.model.common.value.RegistDate;
import manage.store.model.common.value.WeekNumber;
import manage.store.model.common.value.YearMonth;
import manage.store.model.money.sales.value.Money;
import manage.store.testUtils.money.MonthlySalesUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@Tag(Tags.Test.UNIT)
@ExtendWith(MockitoExtension.class)
class SalesSummaryServiceImplTest {

    @InjectMocks
    private SalesSummaryServiceImpl salesSummaryService;


    @Test
    @DisplayName("getMonthSalesSummary 성공 - 모든 일별 매출이 존재하는 경우")
    void getMonthSalesSummary_success_fullSales() {
        // Given
        // 파라미터
        final YearMonth yearMonth = MonthlySalesUtils.YEAR_MONTH;
        final List<BasicDailySales> dummySales = MonthlySalesUtils.BASIC_DAILY_SALES_2023_05();
        final String branchCd = dummySales.get(0).getBranchCd();

        final GetMonthlySalesSummaryParam param = new GetMonthlySalesSummaryParam(branchCd, yearMonth, dummySales);

        // 예상 결과
        // 일별 통계
        final List<SalesDailySummary> expectedDailySummary = MonthlySalesUtils.EXPECTED_DAILY_SUMMARY();

        // 주별 통계
        final List<SalesWeeklySummary> expectedWeeklySummaries = MonthlySalesUtils.EXPECTED_WEEKLY_SUMMARY();

        // 월 총 매출 통게
        final Money expectedMonthTotalCard = MonthlySalesUtils.MONTH_TOTAL_CARD();
        final Money expectedMonthTotalCash = MonthlySalesUtils.MONTH_TOTAL_CASH();
        final Money expectedMonthTotalSales = expectedMonthTotalCard.add(expectedMonthTotalCash);

        // When
        GetMonthlySalesSummaryRslt result = salesSummaryService.getMonthSalesSummary(param);

        // Then
        // 기본
        assertNotNull(result);
        assertNotNull(result.salesDailySummary());
        assertNotNull(result.salesWeeklySummary());
        Assertions.assertThat(result.branchCd()).isEqualTo(branchCd);
        Assertions.assertThat(result.yearMonth()).isEqualTo(yearMonth);

        // 일별 통계
        Assertions.assertThat(result.salesDailySummary().size()).isEqualTo(param.monthDailySales().size());
        for (int i = 0; i < result.salesDailySummary().size(); i++) {
            SalesDailySummary actual = result.salesDailySummary().get(i);
            SalesDailySummary expected = expectedDailySummary.get(i);

            Assertions.assertThat(actual.getBranchCd()).isEqualTo(expected.getBranchCd());
            Assertions.assertThat(actual.getRegistDate()).isEqualTo(expected.getRegistDate());
            Assertions.assertThat(actual.getWeeklyTotalSales()).isEqualTo(expected.getWeeklyTotalSales());
            Assertions.assertThat(actual.getMonthTotalSales()).isEqualTo(expected.getMonthTotalSales());
        }

        // 주별 통계
        Assertions.assertThat(result.salesWeeklySummary().size()).isEqualTo(5);
        for (int i = 0; i < result.salesWeeklySummary().size(); i++) {
            SalesWeeklySummary actual = result.salesWeeklySummary().get(i);
            SalesWeeklySummary expected = expectedWeeklySummaries.get(i);

            Assertions.assertThat(actual.getBranchCd()).isEqualTo(expected.getBranchCd());
            Assertions.assertThat(actual.getYearMonth()).isEqualTo(expected.getYearMonth());
            Assertions.assertThat(actual.getWeekNumber()).isEqualTo(expected.getWeekNumber());
            Assertions.assertThat(actual.getSalesAvg()).isEqualTo(expected.getSalesAvg());
            Assertions.assertThat(actual.getExpectedTotalSales()).isEqualTo(expected.getExpectedTotalSales());
        }

        // 월 총 매출 통계
        Assertions.assertThat(result.monthTotalCash()).isEqualTo(expectedMonthTotalCash);
        Assertions.assertThat(result.monthTotalCard()).isEqualTo(expectedMonthTotalCard);
        Assertions.assertThat(result.monthTotalSales()).isEqualTo(expectedMonthTotalSales);
    }

    @Test
    @DisplayName("getMonthSalesSummary 성공 - 일부 일별 매출이 존재하는 경우")
    void getMonthSalesSummary_success_notFullSales() {
        // Given
        // 파라미터
        final int notEmptySalesCnt = 10;
        final String branchCd = MonthlySalesUtils.BRANCH_CD;
        final YearMonth yearMonth = MonthlySalesUtils.YEAR_MONTH;
        List<BasicDailySales> dummySales = IntStream.range(0, MonthlySalesUtils.BASIC_DAILY_SALES_2023_05().size())
                .mapToObj(idx -> {
                    BasicDailySales sales = MonthlySalesUtils.BASIC_DAILY_SALES_2023_05().get(idx);

                    if (idx <= 9) {
                        return sales;
                    } else {
                        return sales.setCardSales(new Money(0L))
                                .setCashSales(new Money(0L))
                                .setTotalSales(new Money(0L))
                                .setCardPercentage(0);
                    }
                })
                .toList();

        final GetMonthlySalesSummaryParam param = new GetMonthlySalesSummaryParam(branchCd, yearMonth, dummySales);

        // 예상 결과
        // 일별 통계
        final List<SalesDailySummary> dailySummary = MonthlySalesUtils.EXPECTED_DAILY_SUMMARY();
        dailySummary.set(10, new SalesDailySummary(MonthlySalesUtils.BRANCH_CD, new RegistDate("2023-05-11"), new Money(54L), new Money(110L)));
        dailySummary.set(11, new SalesDailySummary(MonthlySalesUtils.BRANCH_CD, new RegistDate("2023-05-12"), new Money(54L), new Money(110L)));
        dailySummary.set(12, new SalesDailySummary(MonthlySalesUtils.BRANCH_CD, new RegistDate("2023-05-13"), new Money(54L), new Money(110L)));
        dailySummary.set(13, new SalesDailySummary(MonthlySalesUtils.BRANCH_CD, new RegistDate("2023-05-14"), new Money(54L), new Money(110L)));
        final List<SalesDailySummary> expectedDailySummary = IntStream.range(0, dailySummary.size())
                        .mapToObj(idx -> {
                            SalesDailySummary currentSales = dailySummary.get(idx);

                            if (idx < 14) {
                                return currentSales;
                            } else {
                                return currentSales
                                        .setWeeklyTotalSales(new Money(0L))
                                        .setMonthTotalSales(new Money(110L));
                            }
                        })
                        .toList();

        // 주별 통계
        final List<SalesWeeklySummary> expectedWeeklySummaries = new ArrayList<>(){{
            add(new SalesWeeklySummary.Builder(MonthlySalesUtils.BRANCH_CD, MonthlySalesUtils.YEAR_MONTH, new WeekNumber(1))
                    .salesAvg(new Money(8L))
                    .expectedTotalSales(new Money(56L))
                    .build());
            add(new SalesWeeklySummary.Builder(MonthlySalesUtils.BRANCH_CD, MonthlySalesUtils.YEAR_MONTH, new WeekNumber(2))
                    .salesAvg(new Money(7L))
                    .expectedTotalSales(new Money(54L))
                    .build());
            add(new SalesWeeklySummary.Builder(MonthlySalesUtils.BRANCH_CD, MonthlySalesUtils.YEAR_MONTH, new WeekNumber(3))
                    .salesAvg(new Money(0L))
                    .expectedTotalSales(new Money(0L))
                    .build());
            add(new SalesWeeklySummary.Builder(MonthlySalesUtils.BRANCH_CD, MonthlySalesUtils.YEAR_MONTH, new WeekNumber(4))
                    .salesAvg(new Money(0L))
                    .expectedTotalSales(new Money(0L))
                    .build());
            add(new SalesWeeklySummary.Builder(MonthlySalesUtils.BRANCH_CD, MonthlySalesUtils.YEAR_MONTH, new WeekNumber(5))
                    .salesAvg(new Money(0L))
                    .expectedTotalSales(new Money(0L))
                    .build());
        }};

        // 월 총 매출 통게
        final Money expectedMonthTotalCard = new Money(55L);
        final Money expectedMonthTotalCash = new Money(55L);
        final Money expectedMonthTotalSales = expectedMonthTotalCard.add(expectedMonthTotalCash);

        // When
        GetMonthlySalesSummaryRslt result = salesSummaryService.getMonthSalesSummary(param);

        // Then
        // 기본
        assertNotNull(result);
        assertNotNull(result.salesDailySummary());
        assertNotNull(result.salesWeeklySummary());
        Assertions.assertThat(result.branchCd()).isEqualTo(branchCd);
        Assertions.assertThat(result.yearMonth()).isEqualTo(yearMonth);

        // 일별 통계
        Assertions.assertThat(result.salesDailySummary().size()).isEqualTo(param.monthDailySales().size());
        for (int i = 0; i < result.salesDailySummary().size(); i++) {
            SalesDailySummary actual = result.salesDailySummary().get(i);
            SalesDailySummary expected = expectedDailySummary.get(i);

            Assertions.assertThat(actual.getBranchCd()).isEqualTo(expected.getBranchCd());
            Assertions.assertThat(actual.getRegistDate()).isEqualTo(expected.getRegistDate());
            Assertions.assertThat(actual.getWeeklyTotalSales()).isEqualTo(expected.getWeeklyTotalSales());
            Assertions.assertThat(actual.getMonthTotalSales()).isEqualTo(expected.getMonthTotalSales());
        }

        // 주별 통계
        Assertions.assertThat(result.salesWeeklySummary().size()).isEqualTo(5);
        for (int i = 0; i < result.salesWeeklySummary().size(); i++) {
            SalesWeeklySummary actual = result.salesWeeklySummary().get(i);
            SalesWeeklySummary expected = expectedWeeklySummaries.get(i);

            Assertions.assertThat(actual.getBranchCd()).isEqualTo(expected.getBranchCd());
            Assertions.assertThat(actual.getYearMonth()).isEqualTo(expected.getYearMonth());
            Assertions.assertThat(actual.getWeekNumber()).isEqualTo(expected.getWeekNumber());
            Assertions.assertThat(actual.getSalesAvg()).isEqualTo(expected.getSalesAvg());
            Assertions.assertThat(actual.getExpectedTotalSales()).isEqualTo(expected.getExpectedTotalSales());
        }

        // 월 총 매출 통계
        Assertions.assertThat(result.monthTotalCash()).isEqualTo(expectedMonthTotalCash);
        Assertions.assertThat(result.monthTotalCard()).isEqualTo(expectedMonthTotalCard);
        Assertions.assertThat(result.monthTotalSales()).isEqualTo(expectedMonthTotalSales);
    }

    @Test
    @DisplayName("getMonthSalesSummary 성공 - 일별 매출이 없는 경우")
    void getMonthSalesSummary_noDailySales() {
        // Given
        // 파라미터
        final YearMonth yearMonth = new YearMonth(2023, 5);
        final List<BasicDailySales> dummySales = new ArrayList<>();
        final String branchCd = "branchCd01";

        final GetMonthlySalesSummaryParam param = new GetMonthlySalesSummaryParam(branchCd, yearMonth, dummySales);

        // 예상 결과
        // 일별 통계
        final List<SalesDailySummary> expectedDailySummary = new ArrayList<>();

        // 주별 통계
        List<SalesWeeklySummary> expectedWeeklySummaries = new ArrayList<>();

        // 월 총 매출 통게
        final Money expectedMonthTotalCard = new Money(0L);
        final Money expectedMonthTotalCash = new Money(0L);
        final Money expectedMonthTotalSales = new Money(0L);

        // When
        GetMonthlySalesSummaryRslt result = salesSummaryService.getMonthSalesSummary(param);

        // Then
        // 기본
        assertNotNull(result);
        assertNotNull(result.salesDailySummary());
        assertNotNull(result.salesWeeklySummary());
        Assertions.assertThat(result.branchCd()).isEqualTo(branchCd);
        Assertions.assertThat(result.yearMonth()).isEqualTo(yearMonth);

        // 일별 통계
        Assertions.assertThat(result.salesDailySummary().size()).isEqualTo(param.monthDailySales().size());

        // 주별 통계
        Assertions.assertThat(result.salesWeeklySummary().size()).isEqualTo(0);

        // 월 총 매출 통계
        Assertions.assertThat(result.monthTotalCash()).isEqualTo(expectedMonthTotalCash);
        Assertions.assertThat(result.monthTotalCard()).isEqualTo(expectedMonthTotalCard);
        Assertions.assertThat(result.monthTotalSales()).isEqualTo(expectedMonthTotalSales);
    }

    @Test
    @DisplayName("getMonthSalesSummary 실패 - 파라미터가 null인 경우")
    void getMonthSalesSummary_invalidParameter() {
        assertThrows(InvalidParameterException.class, () -> {
            salesSummaryService.getMonthSalesSummary(null);
        });
    }
}