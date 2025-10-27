package manage.store.testUtils.money;

import manage.store.dto.money.sales.month.service.BasicDailySales;
import manage.store.dto.money.sales.month.service.SalesDailySummary;
import manage.store.dto.money.sales.month.service.SalesWeeklySummary;
import manage.store.model.common.value.RegistDate;
import manage.store.model.common.value.WeekNumber;
import manage.store.model.common.value.YearMonth;
import manage.store.model.money.sales.DailySales.StoreSales;
import manage.store.model.money.sales.value.Money;
import manage.store.model.user.value.UserId;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MonthlySalesUtils {

    public static final String BRANCH_CD = "branchCd01";

    public static final YearMonth YEAR_MONTH = new YearMonth(2023, 5);

    /**
     * 2023년 5월의 일별 매출 데이터 리스트
     * 일별 카드 / 현금 매출은 일자와 동일
     * Ex) 1일 -> card, cash: 1, total: 2, cardPercentage: 50
     */
    public static final List<BasicDailySales> BASIC_DAILY_SALES_2023_05;

    public static final List<StoreSales> STORE_SALES_2023_05;

    static {
        List<BasicDailySales> salesList = new ArrayList<>();
        List<StoreSales> storeSalesList = new ArrayList<>();

        LocalDate startDate = LocalDate.of(YEAR_MONTH.getYear(), YEAR_MONTH.getMonth(), 1);
        LocalDate endDate = LocalDate.of(YEAR_MONTH.getYear(), YEAR_MONTH.getMonth(), 31);
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            long dayValue = currentDate.getDayOfMonth();

            Money card = new Money(dayValue);
            Money cash = new Money(dayValue);
            Money totalSales = card.add(cash);
            int cardPercentage = (int) ((card.value() * 100) / totalSales.value());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String yyyyMMdd = currentDate.format(formatter);

            RegistDate regDate = new RegistDate(yyyyMMdd);

            BasicDailySales dailySale = new BasicDailySales(
                    BRANCH_CD,
                    regDate,
                    card,
                    cash,
                    totalSales,
                    cardPercentage
            );
            salesList.add(dailySale);

            StoreSales storeSales = new StoreSales();
            storeSales.setBranchCd(BRANCH_CD);
            storeSales.setRegistDate(regDate);
            storeSales.setCardSales(card);
            storeSales.setCashSales(cash);
            storeSales.setComment("comment_" + card.value());
            storeSales.setCreatedBy(new UserId("userId"));
            storeSales.setLastUpdatedBy(new UserId("userId"));
            storeSalesList.add(storeSales);

            currentDate = currentDate.plusDays(1);
        }

        BASIC_DAILY_SALES_2023_05 = Collections.unmodifiableList(salesList);

        STORE_SALES_2023_05 = Collections.unmodifiableList(storeSalesList);
    }

    public static final List<SalesDailySummary> EXPECTED_DAILY_SUMMARY = List.of(
            // --- 1주차
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-01"), new Money(2L), new Money(2L)),
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-02"), new Money(6L), new Money(6L)),
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-03"), new Money(12L), new Money(12L)),
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-04"), new Money(20L), new Money(20L)),
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-05"), new Money(30L), new Money(30L)),
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-06"), new Money(42L), new Money(42L)),
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-07"), new Money(56L), new Money(56L)),

            // --- 2주차
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-08"), new Money(16L), new Money(72L)),
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-09"), new Money(34L), new Money(90L)),
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-10"), new Money(54L), new Money(110L)),
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-11"), new Money(76L), new Money(132L)),
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-12"), new Money(100L), new Money(156L)),
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-13"), new Money(126L), new Money(182L)),
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-14"), new Money(154L), new Money(210L)),

            // --- 3주차
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-15"), new Money(30L), new Money(240L)),
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-16"), new Money(62L), new Money(272L)),
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-17"), new Money(96L), new Money(306L)),
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-18"), new Money(132L), new Money(342L)),
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-19"), new Money(170L), new Money(380L)),
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-20"), new Money(210L), new Money(420L)),
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-21"), new Money(252L), new Money(462L)),

            // --- 4주차
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-22"), new Money(44L), new Money(506L)),
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-23"), new Money(90L), new Money(552L)),
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-24"), new Money(138L), new Money(600L)),
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-25"), new Money(188L), new Money(650L)),
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-26"), new Money(240L), new Money(702L)),
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-27"), new Money(294L), new Money(756L)),
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-28"), new Money(350L), new Money(812L)),

            // --- 5주차
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-29"), new Money(58L), new Money(870L)),
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-30"), new Money(118L), new Money(930L)),
            new SalesDailySummary(BRANCH_CD, new RegistDate("2023-05-31"), new Money(180L), new Money(992L))
    );

    public static final List<SalesWeeklySummary> EXPECTED_WEEKLY_SUMMARY = new ArrayList<>(){{
        add(new SalesWeeklySummary.Builder(BRANCH_CD, YEAR_MONTH, new WeekNumber(1))
                .salesAvg(new Money(8L))
                .expectedTotalSales(new Money(56L))
                .build());
        add(new SalesWeeklySummary.Builder(BRANCH_CD, YEAR_MONTH, new WeekNumber(2))
                .salesAvg(new Money(22L))
                .expectedTotalSales(new Money(154L))
                .build());
        add(new SalesWeeklySummary.Builder(BRANCH_CD, YEAR_MONTH, new WeekNumber(3))
                .salesAvg(new Money(36L))
                .expectedTotalSales(new Money(252L))
                .build());
        add(new SalesWeeklySummary.Builder(BRANCH_CD, YEAR_MONTH, new WeekNumber(4))
                .salesAvg(new Money(50L))
                .expectedTotalSales(new Money(350L))
                .build());
        add(new SalesWeeklySummary.Builder(BRANCH_CD, YEAR_MONTH, new WeekNumber(5))
                .salesAvg(new Money(60L))
                .expectedTotalSales(new Money(420L))
                .build());
    }};

    public static final Money MONTH_TOTAL_CARD = new Money(496L);

    public static final Money MONTH_TOTAL_CASH = new Money(496L);

}
