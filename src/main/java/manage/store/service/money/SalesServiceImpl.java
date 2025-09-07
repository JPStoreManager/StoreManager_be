package manage.store.service.money;

import lombok.RequiredArgsConstructor;
import manage.store.dto.money.month.GetMonthSalesRequest;
import manage.store.dto.money.month.GetMonthSalesResponse;
import manage.store.model.common.value.RegistDate;
import manage.store.repository.money.SalesRepository;
import manage.store.utils.DateUtils;
import manage.store.model.money.sales.DailySales.DailySales;
import manage.store.model.money.sales.value.Money;
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
    public GetMonthSalesResponse getMonthSales(GetMonthSalesRequest request) {
        final String branchCd = request.getBranchCd();
        final int year = request.getYear();
        final int month = request.getMonth();

        if(!(StringUtils.hasText(branchCd) && DateUtils.isYearValid(year) && DateUtils.isMonthValid(month))) {
            throw new IllegalArgumentException("Invalid parameters for getting monthly sales. Branch code: " + branchCd + ", Year: " + year + ", Month: " + month);
        }

        final GetMonthSalesResponse res = getMonthSalesResponseFormat(branchCd, year, month);
        final GetMonthSalesResponse salesUpdatedRes = setMonthSalesResponse(res, branchCd, year, month);
//        final GetMonthSalesResponse salesExpenseUpdatedRes = setMonthSalesExpenseResponse(salesUpdatedRes, branchCd, year, month);

        return salesUpdatedRes;
//        return salesExpenseUpdatedRes;
    }

    /**
     * 월별 매출 응답에 날짜별 매출 데이터를 설정한다.
     * @param response 월별 매출 응답 객체 (월별 날짜에 대한 매출 형식 데이터만 넣어져 있는 상태)
     * @param branchCd 지점 코드
     * @param year 년
     * @param month 월
     * @return 날짜별 매출 데이터가 설정된 GetMonthSalesResponse 객체
     */
    public GetMonthSalesResponse setMonthSalesResponse(GetMonthSalesResponse response, String branchCd, int year, int month) {
        final Map<RegistDate, DailySales> salesByRegisteredDate = salesRepository.selectSalesByMonth(branchCd, year, month).stream()
                .collect(Collectors.toMap(
                        DailySales::getRegistDate,
                        sales -> sales,
                        (existing, replacement) -> existing, // 중복된 키가 있을 경우 기존 값을 유지
                        HashMap::new
                ));

        Money monthlyTotalSales = new Money(0L), weeklyTotalSales = new Money(0L);
        for (GetMonthSalesResponse.DailySales dailySales : response.getMonthlySales()) {
            final DailySales salesData = salesByRegisteredDate.get(dailySales.getRegistDate());
            if(salesData == null) continue;

            final RegistDate registeredDate = salesData.getRegistDate();
            final Money cardSales = salesData.getCardSales(), cashSales = salesData.getCashSales();
            final Money dailyTotalSales = new Money(cardSales.value() + cashSales.value());

            monthlyTotalSales = monthlyTotalSales.add(dailyTotalSales);
            weeklyTotalSales = getWeeklyTotalSales(registeredDate, weeklyTotalSales, dailyTotalSales);

            dailySales.setCardSales(salesData.getCardSales());
            dailySales.setCashSales(salesData.getCashSales());
            dailySales.setTotalSales(dailyTotalSales);
            dailySales.setCardPercentage((int)(cardSales.value() * 100 / dailyTotalSales.value()));
            dailySales.setWeeklyTotalSales(weeklyTotalSales);
            dailySales.setMonthTotalSales(monthlyTotalSales);
            dailySales.setComment(salesData.getComment());
        }

        return response;
    }

    /**
     * 월별 매출 응답의 날짜별 데이터를 생성한다. (실질 데이터는 포함되지 않는다.)
     * @param branchCd 지점 코드
     * @param year 년
     * @param month 월
     * @return GetMonthSalesResponse 날짜별 매출 객체가 생성된 GetMonthSalesResponse 객체
     */
    private GetMonthSalesResponse getMonthSalesResponseFormat(String branchCd, int year, int month) {
        GetMonthSalesResponse response = new GetMonthSalesResponse();

        int lengthOfMonthDays = DateUtils.getDaysCntInMonth(year, month);
        List<GetMonthSalesResponse.DailySales> monthlySales = new ArrayList<>(lengthOfMonthDays);

        for (int i = 1; i <= lengthOfMonthDays; i++) {
            GetMonthSalesResponse.DailySales dailySales = new GetMonthSalesResponse.DailySales();
            RegistDate registDate = new RegistDate(year, month, i);

            dailySales.setBranchCd(branchCd);
            dailySales.setRegistDate(registDate);

            monthlySales.add(dailySales);
        }

        response.setMonthlySales(monthlySales);

        return response;
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

