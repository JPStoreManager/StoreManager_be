package manage.store.dto.money.sales.month;

import lombok.Data;

@Data
public class GetYearSalesRequest extends GetSalesBaseRequest{

    public GetYearSalesRequest(String branchCd, Integer year) {
        super(branchCd, year);
    }

    public GetYearSalesRequest() {}
}
