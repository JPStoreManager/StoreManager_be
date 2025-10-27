package manage.store.service.money.sales;


import manage.store.dto.money.sales.month.controller.GetMonthSalesResponse;
import manage.store.dto.money.sales.month.service.GetMonthlySalesParam;
import manage.store.exception.common.InvalidParameterException;

public interface SalesService {

    /**
     * 월별 매출 조회
     *
     * @param request 월별 매출 조회 요청 정보
     * @return 월별 매출
     * @throws InvalidParameterException 인자가 유효하지 않을 때
     */
     GetMonthSalesResponse getMonthSales(GetMonthlySalesParam request);

}
