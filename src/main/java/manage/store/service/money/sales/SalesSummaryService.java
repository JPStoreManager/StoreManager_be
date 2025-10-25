package manage.store.service.money.sales;

import manage.store.dto.money.month.BasicDailySales;
import manage.store.dto.money.month.SalesDailySummary;
import manage.store.exception.common.InvalidParameterException;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface SalesSummaryService {
    /**
     * 월별 매출 응답의 날짜별 통계 데이터를 설정
     * @param monthlySales 월에 대한 날짜별 매출 객체
     * @return List<SalesSummary> 월별 매출의 통계 데이터
     * @throws InvalidParameterException monthlySales가 null이거나 비어있을 경우
     */
    @PreAuthorize("@salesAccessPolicy.canAccessStatistics()")
    List<SalesDailySummary> getMonthSalesSummary(List<BasicDailySales> monthlySales);

    // 년별 매출 통계 조회
}
