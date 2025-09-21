package manage.store.dto.money.month;

import lombok.*;
import manage.store.dto.common.BaseResponse;
import manage.store.model.common.value.RegistDate;
import manage.store.model.common.value.SuccessFlag;
import manage.store.model.money.sales.value.Money;

import java.util.List;

@Getter
@Setter
@ToString
public class GetMonthSalesResponse extends BaseResponse {

    private List<DailySales> dailySales;

    public GetMonthSalesResponse(SuccessFlag successFlag, String msg, List<DailySales> dailySales) {
        super(successFlag, msg);

        this.dailySales = dailySales;
    }

    @Getter
    @Setter
    @ToString
    public static class DailySales {

        private String branchCd;
        private RegistDate registDate;
        private Money cardSales;
        private Money cashSales;
        private Money totalSales;
        private int cardPercentage;
        private Money weeklyTotalSales;
        private Money monthTotalSales;
        private Money expense;
        private Money monthTotalExpense;
        private Money variableExpense;
        private String comment;

        public DailySales() {
            this.cardSales = new Money(0L);
            this.cashSales = new Money(0L);
            this.totalSales = new Money(0L);
            this.cardPercentage = 0;
            this.weeklyTotalSales = new Money(0L);
            this.monthTotalSales = new Money(0L);
            this.expense = new Money(0L);
            this.monthTotalExpense = new Money(0L);
            this.variableExpense = new Money(0L);
        }

    }

}
