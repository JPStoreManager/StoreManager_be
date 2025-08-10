package manage.store.service.money;


import manage.store.dto.money.month.GetMonthSalesRequest;
import manage.store.dto.money.month.GetMonthSalesResponse;

public interface SalesService {

    /**
     * 월별 매출 조회
     *
     * @param request 월별 매출 조회 요청 정보
     * @return 월별 매출 응답 정보
     * @throws IllegalArgumentException 인자가 유효하지 않을 때
     */
    GetMonthSalesResponse getMonthSales(GetMonthSalesRequest request);

}
