package manage.store.dto.money.sales.month;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetMonthSalesRequest extends GetSalesBaseRequest{

    @Min(value = 1, message = "월은 1 이상이어야 합니다.")
    @Max(value = 12, message = "월은 12 이하이어야 합니다.")
    private Integer month;

    public GetMonthSalesRequest(String branchCd, Integer year, Integer month) {
        super(branchCd, year);
        this.month = month;
    }

}
