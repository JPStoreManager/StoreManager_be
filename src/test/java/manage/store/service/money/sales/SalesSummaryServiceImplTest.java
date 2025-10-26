package manage.store.service.money.sales;

import manage.store.consts.Tags;
import manage.store.dto.money.sales.month.*;
import manage.store.exception.common.InvalidParameterException;
import manage.store.model.common.value.RegistDate;
import manage.store.model.common.value.WeekNumber;
import manage.store.model.common.value.YearMonth;
import manage.store.model.money.sales.value.Money;
import manage.store.testUtils.money.DailySalesUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag(Tags.Test.UNIT)
@ExtendWith(MockitoExtension.class)
class SalesSummaryServiceImplTest {

    @InjectMocks
    private SalesSummaryServiceImpl salesSummaryService;


    @Test
    @DisplayName("getMonthSalesSummary 성공 - 일별 매출이 존재하는 경우")
    void getMonthSalesSummary_success() {
        // Given
        // 파라미터
        final YearMonth yearMonth = new YearMonth(2023, 5);
        final List<BasicDailySales> dummySales = DailySalesUtils.SALES_2023_05;
        final String branchCd = dummySales.get(0).getBranchCd();

        final GetMonthlySalesSummaryParam param = new GetMonthlySalesSummaryParam(branchCd, yearMonth, dummySales);

        // 예상 결과
        // 일별 통계
        final List<SalesDailySummary> expectedDailySummary = List.of(
                // --- 1주차
                new SalesDailySummary(branchCd, new RegistDate("2023-05-01"), new Money(2L), new Money(2L)),
                new SalesDailySummary(branchCd, new RegistDate("2023-05-02"), new Money(6L), new Money(6L)),
                new SalesDailySummary(branchCd, new RegistDate("2023-05-03"), new Money(12L), new Money(12L)),
                new SalesDailySummary(branchCd, new RegistDate("2023-05-04"), new Money(20L), new Money(20L)),
                new SalesDailySummary(branchCd, new RegistDate("2023-05-05"), new Money(30L), new Money(30L)),
                new SalesDailySummary(branchCd, new RegistDate("2023-05-06"), new Money(42L), new Money(42L)),
                new SalesDailySummary(branchCd, new RegistDate("2023-05-07"), new Money(56L), new Money(56L)),

                // --- 2주차
                new SalesDailySummary(branchCd, new RegistDate("2023-05-08"), new Money(16L), new Money(72L)),
                new SalesDailySummary(branchCd, new RegistDate("2023-05-09"), new Money(34L), new Money(90L)),
                new SalesDailySummary(branchCd, new RegistDate("2023-05-10"), new Money(54L), new Money(110L)),
                new SalesDailySummary(branchCd, new RegistDate("2023-05-11"), new Money(76L), new Money(132L)),
                new SalesDailySummary(branchCd, new RegistDate("2023-05-12"), new Money(100L), new Money(156L)),
                new SalesDailySummary(branchCd, new RegistDate("2023-05-13"), new Money(126L), new Money(182L)),
                new SalesDailySummary(branchCd, new RegistDate("2023-05-14"), new Money(154L), new Money(210L)),

                // --- 3주차
                new SalesDailySummary(branchCd, new RegistDate("2023-05-15"), new Money(30L), new Money(240L)),
                new SalesDailySummary(branchCd, new RegistDate("2023-05-16"), new Money(62L), new Money(272L)),
                new SalesDailySummary(branchCd, new RegistDate("2023-05-17"), new Money(96L), new Money(306L)),
                new SalesDailySummary(branchCd, new RegistDate("2023-05-18"), new Money(132L), new Money(342L)),
                new SalesDailySummary(branchCd, new RegistDate("2023-05-19"), new Money(170L), new Money(380L)),
                new SalesDailySummary(branchCd, new RegistDate("2023-05-20"), new Money(210L), new Money(420L)),
                new SalesDailySummary(branchCd, new RegistDate("2023-05-21"), new Money(252L), new Money(462L)),

                // --- 4주차
                new SalesDailySummary(branchCd, new RegistDate("2023-05-22"), new Money(44L), new Money(506L)),
                new SalesDailySummary(branchCd, new RegistDate("2023-05-23"), new Money(90L), new Money(552L)),
                new SalesDailySummary(branchCd, new RegistDate("2023-05-24"), new Money(138L), new Money(600L)),
                new SalesDailySummary(branchCd, new RegistDate("2023-05-25"), new Money(188L), new Money(650L)),
                new SalesDailySummary(branchCd, new RegistDate("2023-05-26"), new Money(240L), new Money(702L)),
                new SalesDailySummary(branchCd, new RegistDate("2023-05-27"), new Money(294L), new Money(756L)),
                new SalesDailySummary(branchCd, new RegistDate("2023-05-28"), new Money(350L), new Money(812L)),

                // --- 5주차
                new SalesDailySummary(branchCd, new RegistDate("2023-05-29"), new Money(58L), new Money(870L)),
                new SalesDailySummary(branchCd, new RegistDate("2023-05-30"), new Money(118L), new Money(930L)),
                new SalesDailySummary(branchCd, new RegistDate("2023-05-31"), new Money(180L), new Money(992L))
        );

        // 주별 통계
        List<SalesWeeklySummary> expectedWeeklySummaries = new ArrayList<>(){{
            add(new SalesWeeklySummary.Builder(branchCd, yearMonth, new WeekNumber(1))
                    .salesAvg(new Money(8L))
                    .expectedTotalSales(new Money(56L))
                    .build());
            add(new SalesWeeklySummary.Builder(branchCd, yearMonth, new WeekNumber(2))
                    .salesAvg(new Money(22L))
                    .expectedTotalSales(new Money(154L))
                    .build());
            add(new SalesWeeklySummary.Builder(branchCd, yearMonth, new WeekNumber(3))
                    .salesAvg(new Money(36L))
                    .expectedTotalSales(new Money(252L))
                    .build());
            add(new SalesWeeklySummary.Builder(branchCd, yearMonth, new WeekNumber(4))
                    .salesAvg(new Money(50L))
                    .expectedTotalSales(new Money(350L))
                    .build());
            add(new SalesWeeklySummary.Builder(branchCd, yearMonth, new WeekNumber(5))
                    .salesAvg(new Money(60L))
                    .expectedTotalSales(new Money(420L))
                    .build());
        }};

        // 월 총 매출 통게
        final Money expectedMonthTotalCard = new Money(496L);
        final Money expectedMonthTotalCash = new Money(496L);
        final Money expectedMonthTotalSales = new Money(992L);

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