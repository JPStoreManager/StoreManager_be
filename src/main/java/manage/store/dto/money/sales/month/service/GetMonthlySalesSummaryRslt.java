package manage.store.dto.money.sales.month.service;

import manage.store.exception.common.InvalidParameterException;
import manage.store.model.common.value.YearMonth;
import manage.store.model.money.sales.value.Money;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 월별 매출 요약 결과 DTO
 */
public record GetMonthlySalesSummaryRslt(String branchCd, YearMonth yearMonth,
                                         List<SalesDailySummary> salesDailySummary, List<SalesWeeklySummary> salesWeeklySummary,
                                         Money monthTotalCard, Money monthTotalCash, Money monthTotalSales) {

    public GetMonthlySalesSummaryRslt {
        if (!StringUtils.hasText(branchCd) || yearMonth == null ||
            salesDailySummary == null || salesWeeklySummary == null ||
            monthTotalCard == null || monthTotalCash == null || monthTotalSales == null
        ) {
            throw new InvalidParameterException("Invalid parameters for GetMonthlySalesSummaryRslt. Branch code: " + branchCd + ", YearMonth: " + yearMonth +
                    ", DailySummary: " + salesDailySummary + ", WeeklySummary: " + salesWeeklySummary +
                    ", MonthTotalCard: " + monthTotalCard + ", MonthTotalCash: " + monthTotalCash + ", MonthTotalSales: " + monthTotalSales);
        }
    }

}
