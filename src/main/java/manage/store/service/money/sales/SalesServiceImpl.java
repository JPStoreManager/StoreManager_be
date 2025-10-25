package manage.store.service.money.sales;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import manage.store.dto.money.month.BasicDailySales;
import manage.store.dto.money.month.GetMonthSalesRequest;
import manage.store.dto.money.month.GetMonthSalesResponse;
import manage.store.dto.money.month.SalesDailySummary;
import manage.store.exception.common.InvalidParameterException;
import manage.store.model.common.value.RegistDate;
import manage.store.repository.money.SalesRepository;
import manage.store.utils.DateUtils;
import manage.store.model.money.sales.DailySales.StoreSales;
import manage.store.model.money.sales.value.Money;
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
    public List<GetMonthSalesResponse.DailySales> getMonthSales(GetMonthSalesRequest request) {
        final String branchCd = request.getBranchCd();
        final int year = request.getYear();
        final int month = request.getMonth();

        if(!(StringUtils.hasText(branchCd) && DateUtils.isYearValid(year) && DateUtils.isMonthValid(month))) {
            throw new InvalidParameterException("Invalid parameters for getting monthly sales. Branch code: " + branchCd + ", Year: " + year + ", Month: " + month);
        }

        // 기본 매출 및 통계 데이터 조회
        final List<BasicDailySales> monthlyBasicSales = getBasicMonthSalesResponse(branchCd, year, month);
        List<SalesDailySummary> monthlySalesSummary = new ArrayList<>();
        try {monthlySalesSummary = salesSummaryService.getMonthSalesSummary(monthlyBasicSales);}
        catch (Exception e) {
            log.info("권한 부족으로 인한 매출 통계 조회 실패");
        }
        // TODO 지출에 대한 통계 추가 필요

        final List<GetMonthSalesResponse.DailySales> result = getMonthSalesResponse(monthlyBasicSales, monthlySalesSummary);

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
     * 월별 기본 매출 데이터와 통계 데이터를 합친 response 객체를 생성
     * @param monthBasicDailySales 월별 기초 매출 데이터
     * @param monthSalesDailySummary 월별 매출 통계 데이터
     * @return List<GetMonthSalesResponse.DailySales> 월별 매출 응답의 날짜별 통계 데이터가 설정된 통합 데이터 리스트
     */
    private List<GetMonthSalesResponse.DailySales> getMonthSalesResponse(List<BasicDailySales> monthBasicDailySales, List<SalesDailySummary> monthSalesDailySummary) {
        List<GetMonthSalesResponse.DailySales> monthSalesResponse = new ArrayList<>(monthBasicDailySales.size());
        for (int i = 0; i < monthBasicDailySales.size(); i++) {
            BasicDailySales basicDailySales = monthBasicDailySales.get(i);
            // 기본 매출 데이터 설정
            GetMonthSalesResponse.DailySales dailySalesResponse = new GetMonthSalesResponse.DailySales();
            dailySalesResponse.setBranchCd(basicDailySales.getBranchCd());
            dailySalesResponse.setRegistDate(basicDailySales.getRegistDate());
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

            monthSalesResponse.add(dailySalesResponse);
        }

        return monthSalesResponse;
    }
}

