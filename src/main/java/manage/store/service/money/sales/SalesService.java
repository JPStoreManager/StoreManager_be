package manage.store.service.money.sales;


import manage.store.dto.money.month.GetMonthSalesRequest;
import manage.store.dto.money.month.GetMonthSalesResponse;
import manage.store.exception.common.InvalidParameterException;

import java.util.List;

public interface SalesService {

    /**
     * 월별 매출 조회
     *
     * @param request 월별 매출 조회 요청 정보
     * @return 월별 매출
     * @throws InvalidParameterException 인자가 유효하지 않을 때
     */
    List<GetMonthSalesResponse.DailySales> getMonthSales(GetMonthSalesRequest request);

}
