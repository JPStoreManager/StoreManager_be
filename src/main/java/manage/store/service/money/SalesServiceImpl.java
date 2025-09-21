package manage.store.service.money;

import lombok.RequiredArgsConstructor;
import manage.store.dto.money.month.GetMonthSalesRequest;
import manage.store.dto.money.month.GetMonthSalesResponse;
import manage.store.model.common.value.RegistDate;
import manage.store.repository.money.SalesRepository;
import manage.store.utils.DateUtils;
import manage.store.model.money.sales.DailySales.DailySales;
import manage.store.model.money.sales.value.Money;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesServiceImpl implements SalesService {

    private final SalesRepository salesRepository;

    @Override
    public List<GetMonthSalesResponse.DailySales> getMonthSales(GetMonthSalesRequest request) {
        final String branchCd = request.getBranchCd();
        final int year = request.getYear();
        final int month = request.getMonth();

        if(!(StringUtils.hasText(branchCd) && DateUtils.isYearValid(year) && DateUtils.isMonthValid(month))) {
            throw new IllegalArgumentException("Invalid parameters for getting monthly sales. Branch code: " + branchCd + ", Year: " + year + ", Month: " + month);
        }

        final List<GetMonthSalesResponse.DailySales> monthSales = getDefaultMonthSalesResponse(branchCd, year, month);
        final List<GetMonthSalesResponse.DailySales> summaryAddedMonthSales = setMonthSalesSummary(monthSales);
//        final GetMonthSalesResponse salesExpenseUpdatedRes = setMonthSalesExpenseResponse(salesUpdatedRes, branchCd, year, month);

        return summaryAddedMonthSales;
    }

    /**
     * 월별 매출 응답의 날짜별 데이터를 생성한다. (실질 데이터는 포함되지 않는다.)
     * @param branchCd 지점 코드
     * @param year 년
     * @param month 월
     * @return List<GetMonthSalesResponse.DailySales> 월에 대한 날짜별 매출 객체
     */
    private List<GetMonthSalesResponse.DailySales> getDefaultMonthSalesResponse(String branchCd, int year, int month) {
        int lengthOfMonthDays = DateUtils.getDaysCntInMonth(year, month);
        List<GetMonthSalesResponse.DailySales> monthlySales = new ArrayList<>(lengthOfMonthDays);
        Map<RegistDate, DailySales> salesByRegisteredDate = getSalesByRegisteredDate(branchCd, year, month);

        for (int i = 1; i <= lengthOfMonthDays; i++) {
            GetMonthSalesResponse.DailySales dailySales = new GetMonthSalesResponse.DailySales();
            RegistDate registDate = new RegistDate(year, month, i);

            // 기본 값 설정
            dailySales.setBranchCd(branchCd);
            dailySales.setRegistDate(registDate);

            // 기본 매출 값 설정
            DailySales salesData = salesByRegisteredDate.get(registDate);
            if(salesData != null) {
                Money totalSales = new Money(salesData.getCardSales().value() + salesData.getCashSales().value());
                int cardPercentage = (int) (salesData.getCardSales().value() * 100 / totalSales.value());

                dailySales.setCardSales(salesData.getCardSales());
                dailySales.setCashSales(salesData.getCashSales());
                dailySales.setTotalSales(totalSales);
                dailySales.setCardPercentage(cardPercentage);
                dailySales.setComment(salesData.getComment());
            }

            monthlySales.add(dailySales);
        }

        return monthlySales;
    }

    /**
     * 월별 매출 응답의 날짜별 통계 데이터를 설정
     * @param monthlySales 월에 대한 날짜별 매출 객체
     * @return List<GetMonthSalesResponse.DailySales> 통계 데이터가 설정된 월에 대한 날짜별 매출 객체
     */
    @PreAuthorize("salesAccessPolicy.canAccessStatistics()")
    private List<GetMonthSalesResponse.DailySales> setMonthSalesSummary(List<GetMonthSalesResponse.DailySales> monthlySales) {
        Money monthlyTotalSales = new Money(0L), weeklyTotalSales = new Money(0L);
        for (GetMonthSalesResponse.DailySales dailySales : monthlySales) {
            RegistDate registeredDate = dailySales.getRegistDate();
            Money dailyTotalSales = dailySales.getTotalSales();

            monthlyTotalSales = monthlyTotalSales.add(dailyTotalSales);
            weeklyTotalSales = getWeeklyTotalSales(registeredDate, weeklyTotalSales, dailyTotalSales);

            dailySales.setWeeklyTotalSales(weeklyTotalSales);
            dailySales.setMonthTotalSales(monthlyTotalSales);
        }
        return monthlySales;
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

    private Map<RegistDate, DailySales> getSalesByRegisteredDate(String branchCd, int year, int month) {
        return salesRepository.selectSalesByMonth(branchCd, year, month).stream()
                .collect(Collectors.toMap(
                        DailySales::getRegistDate,
                        sales -> sales,
                        (existing, replacement) -> existing, // 중복된 키가 있을 경우 기존 값을 유지
                        HashMap::new
                ));
    }
}

