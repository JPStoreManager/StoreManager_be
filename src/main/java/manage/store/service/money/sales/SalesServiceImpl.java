package manage.store.service.money.sales;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import manage.store.dto.money.sales.month.controller.GetMonthSalesRequest;
import manage.store.dto.money.sales.month.controller.GetMonthSalesResponse;
import manage.store.dto.money.sales.month.service.*;
import manage.store.exception.common.InvalidParameterException;
import manage.store.model.common.value.RegistDate;
import manage.store.model.common.value.WeekNumber;
import manage.store.model.common.value.YearMonth;
import manage.store.repository.money.SalesRepository;
import manage.store.utils.DateUtils;
import manage.store.model.money.sales.DailySales.StoreSales;
import manage.store.model.money.sales.value.Money;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalesServiceImpl implements SalesService {

    private final SalesRepository salesRepository;

    private final SalesSummaryService salesSummaryService;

    @Override
    public GetMonthSalesResponse getMonthSales(GetMonthlySalesParam request) {
        if(request == null) {
            throw new InvalidParameterException("Invalid parameters for getting monthly sales. param: " + request);
        }

        final String branchCd = request.branchCd();
        final YearMonth yearMonth = request.yearMonth();

        // 기본 매출 데이터 조회
        final List<BasicDailySales> monthDailyBasicSales = getBasicMonthSalesResponse(branchCd, yearMonth.getYear(), yearMonth.getMonth());
        GetMonthlySalesSummaryRslt monthlySalesSummary = null;
        try {
            // 매출 통계 데이터 조회
            GetMonthlySalesSummaryParam param = new GetMonthlySalesSummaryParam(branchCd, yearMonth, monthDailyBasicSales);
            monthlySalesSummary = salesSummaryService.getMonthSalesSummary(param);}
        catch (AuthorizationDeniedException e) {
            log.info("권한 부족으로 인한 매출 통계 조회 실패");
        }

        // TODO 지출에 대한 통계 추가 필요

        final GetMonthSalesResponse result = getMonthSalesResponse(branchCd, yearMonth, monthDailyBasicSales, monthlySalesSummary);

        return result;
    }

    /**
     * 월별 매출 응답의 날짜별 base 데이터를 생성한다.
     * @param branchCd 지점 코드
     * @param year 년
     * @param month 월
     * @return List<GetMonthSalesResponse.DailySales> 월에 대한 날짜별 매출 객체
     */
    private List<BasicDailySales> getBasicMonthSalesResponse(String branchCd, int year, int month) {
        int lengthOfMonthDays = DateUtils.getDaysCntInMonth(year, month);
        List<BasicDailySales> monthlySales = new ArrayList<>(lengthOfMonthDays);
        Map<RegistDate, StoreSales> salesByRegisteredDate = getSalesByRegisteredDate(branchCd, year, month);

        for (int i = 1; i <= lengthOfMonthDays; i++) {
            RegistDate registDate = new RegistDate(year, month, i);

            // 기본 매출 값 설정
            BasicDailySales dailySales;
            StoreSales salesData = salesByRegisteredDate.get(registDate);
            if(salesData != null) {
                Money totalSales = new Money(salesData.getCardSales().value() + salesData.getCashSales().value());
                int cardPercentage = (int) (salesData.getCardSales().value() * 100 / totalSales.value());

                //
                dailySales = new BasicDailySales(
                        branchCd,
                        registDate,
                        salesData.getCardSales(),
                        salesData.getCashSales(),
                        totalSales,
                        cardPercentage
                    );
            }
            else {
                dailySales = new BasicDailySales(branchCd, registDate);
            }

            monthlySales.add(dailySales);
        }

        return monthlySales;
    }

    private Map<RegistDate, StoreSales> getSalesByRegisteredDate(String branchCd, int year, int month) {
        return salesRepository.selectSalesByMonth(branchCd, year, month).stream()
                .collect(Collectors.toMap(
                        StoreSales::getRegistDate,
                        sales -> sales,
                        (existing, replacement) -> existing, // 중복된 키가 있을 경우 기존 값을 유지
                        HashMap::new
                ));
    }

    /**
     * 월별 매출 및 지출 응답 객체 생성
     * @param branchCd 지점 코드
     * @param yearMonth 년월
     * @param monthBasicDailySales 월에 대한 일별 기본 매출
     * @param monthSalesSummary 월에 대한 매출 통계 데이터
     * @return GetMonthSalesResponse 월별 매출 응답 객체
     */
    private GetMonthSalesResponse getMonthSalesResponse(String branchCd, YearMonth yearMonth,
                                                        List<BasicDailySales> monthBasicDailySales,
                                                        GetMonthlySalesSummaryRslt monthSalesSummary) {
        // 일별 매출 통합
        List<GetMonthSalesResponse.DailySales> monthSalesDailyRes = getMonthSalesDailyResponse(monthBasicDailySales, monthSalesSummary.salesDailySummary());

        // 주별 매출 통합
        List<GetMonthSalesResponse.WeeklySales> monthSalesWeeklyRes = getMonthlySalesWeeklyResponse(monthSalesSummary.salesWeeklySummary());

        GetMonthSalesResponse response = new GetMonthSalesResponse(
                branchCd,
                yearMonth,
                monthSalesDailyRes,
                monthSalesWeeklyRes,
                monthSalesSummary.monthTotalCard(),
                monthSalesSummary.monthTotalCash(),
                monthSalesSummary.monthTotalSales()
        );

        return response;
    }

    /**
     * 월에 대한 일별 기본 매출 및 지출 데이터와 통계 데이터를 합친 일별 통합 데이터를 생성
     * @param monthBasicDailySales 월에 대한 일별 기본 매출
     * @param monthSalesDailySummary 월에 대한 일별 통계 매출
     * @return 기본과 통계 데이터가 합쳐진 일별 매출 / 지출 통합 데이터
     */
    private List<GetMonthSalesResponse.DailySales> getMonthSalesDailyResponse(List<BasicDailySales> monthBasicDailySales, List<SalesDailySummary> monthSalesDailySummary) {
        List<GetMonthSalesResponse.DailySales> monthSalesDailyRes = new ArrayList<>(monthBasicDailySales.size());
        for (int i = 0; i < monthBasicDailySales.size(); i++) {
            BasicDailySales basicDailySales = monthBasicDailySales.get(i);
            // 기본 매출 데이터 설정
            GetMonthSalesResponse.DailySales dailySalesResponse = new GetMonthSalesResponse.DailySales(basicDailySales.getBranchCd(), basicDailySales.getRegistDate());
            dailySalesResponse.setCardSales(basicDailySales.getCardSales());
            dailySalesResponse.setCashSales(basicDailySales.getCashSales());
            dailySalesResponse.setTotalSales(basicDailySales.getTotalSales());
            dailySalesResponse.setCardPercentage(basicDailySales.getCardPercentage());

            // 통계 데이터 설정
            if(i < monthSalesDailySummary.size()) {
                SalesDailySummary salesDailySummary = monthSalesDailySummary.get(i);

                dailySalesResponse.setWeeklyTotalSales(salesDailySummary.getWeeklyTotalSales());
                dailySalesResponse.setMonthTotalSales(salesDailySummary.getMonthTotalSales());
            }

            // TODO 지출 관련 통계 추가 필요

            monthSalesDailyRes.add(dailySalesResponse);
        }

        return monthSalesDailyRes;
    }

    /**
     * 월별 매출 및 지출에 대한 주간 통계 응답 객체를 생성
     * @return List<GetMonthSalesResponse.WeeklySales> 월별 매출 및 지출에 대한 주간 통계 응답 리스트
     */
    private List<GetMonthSalesResponse.WeeklySales> getMonthlySalesWeeklyResponse(List<SalesWeeklySummary> monthSalesWeeklySummary) {
        List<GetMonthSalesResponse.WeeklySales> monthSalesWeeklyRes = new ArrayList<>();
        for (SalesWeeklySummary salesWeeklySummary : monthSalesWeeklySummary) {
            String branchCd = salesWeeklySummary.getBranchCd();
            YearMonth yearMonth = salesWeeklySummary.getYearMonth();
            WeekNumber weekNumber = salesWeeklySummary.getWeekNumber();

            GetMonthSalesResponse.WeeklySales weeklySales = new GetMonthSalesResponse.WeeklySales(branchCd, yearMonth, weekNumber);
            weeklySales.setSalesAvg(salesWeeklySummary.getSalesAvg());
            weeklySales.setExpectedTotalSales(salesWeeklySummary.getExpectedTotalSales());

            // TODO 지출 관련 통계 추가 필요

            monthSalesWeeklyRes.add(weeklySales);
        }

        return monthSalesWeeklyRes;
    }
}

