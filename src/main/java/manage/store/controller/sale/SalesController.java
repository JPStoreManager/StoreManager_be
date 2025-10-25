package manage.store.controller.sale;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import manage.store.controller.BaseController;
import manage.store.dto.common.ApiResponse;
import manage.store.dto.money.month.GetMonthSalesRequest;
import manage.store.dto.money.month.GetMonthSalesResponse;
import manage.store.service.money.sales.SalesService;
import manage.store.utils.ApiPathUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SalesController extends BaseController {

    private final SalesService salesService;

//    @GetMapping(ApiPathUtils.ApiPath.Sales.SALES_PAGE_OPTIONS)
//    public GetMonthSalesResponse getSalesPageOptions() {
//        // TODO[권한] 사용자 권한에 따라 다른 페이지 옵션을 반환하도록 구현
//
//        return salesService.getSalesPageOptions();
//    }

    @GetMapping(ApiPathUtils.ApiPath.Sales.SALES_MONTH)
    public ResponseEntity<ApiResponse<GetMonthSalesResponse>> getMonthlySales(@ModelAttribute @Valid GetMonthSalesRequest request) {
        List<GetMonthSalesResponse.DailySales> dailySales = salesService.getMonthSales(request);

        return ResponseEntity.ok(ApiResponse.success(new GetMonthSalesResponse(dailySales), "월 매출 조회 성공"));
    }

//    @GetMapping(ApiPathUtils.ApiPath.Sales.SALES_YEAR)
//    public GetMonthSalesResponse getMonthlySales(@ModelAttribute @Valid GetMonthSalesRequest request) {
//        return salesService.getMonthSales(request);
//    }

}
