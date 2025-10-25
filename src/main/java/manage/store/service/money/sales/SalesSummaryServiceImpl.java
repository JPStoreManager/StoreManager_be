package manage.store.service.money.sales;

import lombok.RequiredArgsConstructor;
import manage.store.dto.money.month.BasicDailySales;
import manage.store.dto.money.month.SalesDailySummary;
import manage.store.exception.common.InvalidParameterException;
import manage.store.model.common.value.RegistDate;
import manage.store.model.money.sales.value.Money;
import manage.store.utils.DateUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesSummaryServiceImpl implements SalesSummaryService {

    /**
     * 월별 매출 응답의 날짜별 통계 데이터를 설정
     * @param monthlySales 월에 대한 날짜별 매출 객체
     * @return List<SalesSummary> 월별 매출의 통계 데이터
     * @throws InvalidParameterException monthlySales가 null이거나 비어있을 경우
     */
    @Override
    @PreAuthorize("salesAccessPolicy.canAccessStatistics()")
    public List<SalesDailySummary> getMonthSalesSummary(List<BasicDailySales> monthlySales) {
        if(monthlySales == null || monthlySales.isEmpty()){
            throw new InvalidParameterException("monthlySales cannot be null or empty");
        }

        Money monthlyTotalSales = new Money(0L), weeklyTotalSales = new Money(0L);
        List<SalesDailySummary> salesDailySummary = new ArrayList<>(monthlySales.size());
        for (BasicDailySales dailySales : monthlySales) {
            RegistDate registeredDate = dailySales.getRegistDate();

            Money dailyTotalSales = dailySales.getTotalSales();
            weeklyTotalSales = getWeeklyTotalSales(registeredDate, weeklyTotalSales, dailyTotalSales);
            monthlyTotalSales = monthlyTotalSales.add(dailyTotalSales);

            SalesDailySummary dailySalesDailySummary = new SalesDailySummary(dailySales.getBranchCd(), registeredDate, weeklyTotalSales, monthlyTotalSales);

            salesDailySummary.add(dailySalesDailySummary);
        }

        return salesDailySummary;
    }

    /**
     * 주간 총 매출을 계산한다.
     * 월요일을 주의 시작점으로 판단한다.
     * @param registeredDate 매출 날짜
     * @param curWeeklyTotalSales 현재 주간 총 매출
     * @param daySales 특정 날짜의 매출
     * @return 주간 총 매출
     */
    private Money getWeeklyTotalSales(RegistDate registeredDate, Money curWeeklyTotalSales, Money daySales) {
        DayOfWeek dayOfWeek = DateUtils.getDayOfWeek(registeredDate.value());
        if(dayOfWeek == DayOfWeek.MONDAY) {
            return daySales;
        } else {
            return curWeeklyTotalSales.add(daySales);
        }
    }

}
