package manage.store.dto.money.sales.month;

import manage.store.exception.common.InvalidParameterException;
import manage.store.model.common.value.YearMonth;
import org.springframework.util.StringUtils;

import java.util.List;

public record GetMonthlySalesSummaryParam(String branchCd, YearMonth yearMonth, List<BasicDailySales> monthDailySales) {

    public GetMonthlySalesSummaryParam {
        if (!StringUtils.hasText(branchCd) || yearMonth == null || monthDailySales == null) {
            throw new InvalidParameterException("Invalid parameters for GetMonthlySalesSummaryParam. Branch code: " + branchCd + ", YearMonth: " + yearMonth + ", MonthlySales: " + monthDailySales);
        }
    }

}
