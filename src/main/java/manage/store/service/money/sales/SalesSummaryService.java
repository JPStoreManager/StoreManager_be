package manage.store.service.money.sales;

import manage.store.dto.money.sales.month.service.GetMonthlySalesSummaryParam;
import manage.store.dto.money.sales.month.service.GetMonthlySalesSummaryRslt;
import manage.store.exception.common.InvalidParameterException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDeniedException;

public interface SalesSummaryService {

    /**
     * 월별 매출 응답의 날짜별 통계 데이터를 설정
     * @param param 월에 대한 일별 매출을 포함한 파라미터 객체
     * @return 월별 매출의 통계 데이터
     * @throws InvalidParameterException param이 null일 경우
     * @throws AuthorizationDeniedException 통게 데이터에 대한 접근 권한이 없을 경우
     */
    @PreAuthorize("@salesAccessPolicy.canAccessStatistics()")
    GetMonthlySalesSummaryRslt getMonthSalesSummary(GetMonthlySalesSummaryParam param);

    // 년별 매출 통계 조회

}
