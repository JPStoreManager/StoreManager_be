package manage.store.dto.money.month;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class GetSalesBaseRequest {

    @NotBlank(message = "지점 코드는 비어있을 수 없습니다.")
    protected String branchCd;

    @Positive(message = "연도는 1 이상의 정수여야 합니다.")
    protected Integer year;

    public GetSalesBaseRequest(String branchCd, Integer year) {
        this.branchCd = branchCd;
        this.year = year;
    }

}
