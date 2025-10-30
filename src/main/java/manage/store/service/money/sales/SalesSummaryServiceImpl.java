package manage.store.service.money.sales;

import lombok.RequiredArgsConstructor;
import manage.store.dto.money.sales.month.service.*;
import manage.store.exception.common.InvalidParameterException;
import manage.store.model.common.value.RegistDate;
import manage.store.model.common.value.WeekNumber;
import manage.store.model.common.value.YearMonth;
import manage.store.model.money.sales.value.Money;
import manage.store.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesSummaryServiceImpl implements SalesSummaryService {

    @Override
    public GetMonthlySalesSummaryRslt getMonthSalesSummary(GetMonthlySalesSummaryParam param) {
        if(param == null) throw new InvalidParameterException("monthlySales cannot be null or empty");

        // 일간 매출 통계
        List<SalesDailySummary> dailySalesSummary = getDailySalesSummary(param.monthDailySales());

        // 주간 매출 통계
        List<SalesWeeklySummary> weeklySalesSummary = getWeeklySalesSummary(param.monthDailySales());

        // 월 총 매출 통계
        Money monthTotalCard = new Money(0L), monthTotalCash = new Money(0L);
        for (BasicDailySales dailySales : param.monthDailySales()) {
            Money cardSales = dailySales.getCardSales();
            Money cashSales = dailySales.getCashSales();

            monthTotalCard = monthTotalCard.add(cardSales);
            monthTotalCash = monthTotalCash.add(cashSales);
        }
        Money monthTotalSales = monthTotalCard.add(monthTotalCash);

        GetMonthlySalesSummaryRslt result = new GetMonthlySalesSummaryRslt(
                param.branchCd(), param.yearMonth(),
                dailySalesSummary, weeklySalesSummary,
                monthTotalCard, monthTotalCash, monthTotalSales
        );

        return result;
    }

    /**
     * 일별 매출 통계 생성
     * @param monthDailySales 월에 대한 일별 매출
     * @return List<SalesDailySummary> 월에 대한 일별 매출 통계
     */
    private List<SalesDailySummary> getDailySalesSummary(List<BasicDailySales> monthDailySales) {
        Money monthlyTotalSales = new Money(0L), weeklyTotalSales = new Money(0L);
        List<SalesDailySummary> salesDailySummary = new ArrayList<>(monthDailySales.size());
        for (BasicDailySales dailySales : monthDailySales) {
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
     * 월 매출에 대한 주간 통계 생성
     * @param monthDailySales 월별 기본 일별 매출
     * @return 월에 대한 주간 통계
     */
    private List<SalesWeeklySummary> getWeeklySalesSummary(List<BasicDailySales> monthDailySales) {
        List<SalesWeeklySummary> salesWeeklySummary = new ArrayList<>();

        Money curWeekSalesTotal = new Money(0L);
        WeekNumber curWeekNumber = null;
        for (BasicDailySales dailySales : monthDailySales) {
            LocalDate registDate = dailySales.getRegistDate().convertToLocalDate();
            WeekNumber weekNumber = new WeekNumber(registDate);

            // 주가 바뀌었을 경우 이전 주차에 대한 데이터 초기화 및 신규 주차에 대한 정보 삽입
            if(!weekNumber.equals(curWeekNumber)) {
                // 새로운 주간 매출 데이터 초기화
                curWeekNumber = weekNumber;
                curWeekSalesTotal = new Money(0L);

                // 신규 주간 매출 통계 추가
                SalesWeeklySummary weeklySummary = new SalesWeeklySummary.Builder(
                    dailySales.getBranchCd(),
                    new YearMonth(registDate.getYear(), registDate.getMonthValue()),
                    weekNumber
                ).build();
                salesWeeklySummary.add(weeklySummary);
            }

            // 현재 주간 총 매출 누적
            curWeekSalesTotal = curWeekSalesTotal.add(dailySales.getTotalSales());

            // 주간 평균 일 매출 계산
            // 1) 월요일부터 현재까지 몇 일 지났는지 일 수 계산
            LocalDate now = DateUtils.nowDate();
            DayOfWeek curDayOfWeek = registDate.getDayOfWeek();
            int curDayPassedAtWeekCnt = curDayOfWeek.getValue();
            // 특정 매출 주 데이터에서 오늘 날짜가 아직 일요일까지 완성되지 않았을 경우 오늘 날짜에 대한 지난 일수를 사용
            // (매출이 발생한 날짜에 대한 집계를 하기 위함)
            if(DateUtils.isOnSameWeek(now, registDate) && registDate.isAfter(now)) {
                curDayPassedAtWeekCnt = now.getDayOfWeek().getValue();
            }

            // 2) 주간 총 매출 / 매출이 발생한 날짜 수를 통해 주간 평균 일 매출 계산
            Money weeklyAvgSales = new Money(curWeekSalesTotal.value() / curDayPassedAtWeekCnt);

            // 주간 예측 매출 계산
            // 1) 월요일부터 현재까지 몇 일 지났는지 일 수 계산 == curDayPassedAtWeekCnt

            // 2) (주간 총 매출 / 날짜 수) * 7일을 통해 주간 예측 매출 계산
            Money expectedTotalSales = new Money(weeklyAvgSales.value() * 7);

            // 현재 계산중인 날짜가 일요일이거나 마지막 날이고 오늘 이전(이하)의 날이면 실제 주간 총 매출을 사용
            // 아직 오늘이 일주일이 채워지기 전이라면 예측 데이터를 써야하고, 그 외 경우에는 모든 실제 매출 데이터를 사용
            if(!DateUtils.isOnSameWeek(now, registDate) &&
                    (curDayOfWeek == DayOfWeek.SUNDAY || registDate.getDayOfMonth() == registDate.lengthOfMonth())) {
                expectedTotalSales = new Money(curWeekSalesTotal.value());
            }

            // 주간 통계 업데이트
            int idx = weekNumber.value() - 1;
            SalesWeeklySummary weeklySummary = salesWeeklySummary.get(idx);
            weeklySummary = weeklySummary.setSalesAvg(weeklyAvgSales);
            weeklySummary = weeklySummary.setExpectedTotalSales(expectedTotalSales);

            salesWeeklySummary.set(idx, weeklySummary);
        }

        return salesWeeklySummary;
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
