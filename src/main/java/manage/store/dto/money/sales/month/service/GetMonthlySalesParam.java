package manage.store.dto.money.sales.month.service;

import manage.store.exception.common.InvalidParameterException;
import manage.store.model.common.value.YearMonth;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

public record GetMonthlySalesParam(String branchCd, YearMonth yearMonth) {

    public GetMonthlySalesParam {
        if(!StringUtils.hasText(branchCd) && yearMonth == null) {
            throw new InvalidParameterException("branch code or yearMonth must not be null");
        }
    }

}
