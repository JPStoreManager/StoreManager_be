package manage.store.dto.money.sales.month;

import lombok.*;
import manage.store.model.common.value.RegistDate;
import manage.store.model.common.value.WeekNumber;
import manage.store.model.common.value.YearMonth;
import manage.store.model.money.sales.value.Money;

import java.util.List;

@Data
@AllArgsConstructor
public class GetMonthSalesResponse{

    private String branchCd;
    private YearMonth yearMonth;
    private List<DailySales> dailySales;
    private List<WeeklySales> weeklySales;
    private Money monthTotalCard;
    private Money monthTotalCash;
    private Money monthTotalSales;

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

        public DailySales(@NonNull String branchCd, @NonNull RegistDate registDate) {
            this.branchCd = branchCd;
            this.registDate = registDate;

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

    @Getter
    @Setter
    @ToString
    public static class WeeklySales {

        private String branchCd;
        private YearMonth yearMonth;
        private WeekNumber weekNumber;
        // 주간 평균 매출
        private Money salesAvg;
        // 주간 예상 매출 -- 아직 한 주가 다 지나지 않은 경우에 활용
        private Money expectedTotalSales;
        // 주간 총 지출
        private Money totalExpense;
        // 인건비
        private Money humanResourceExpense;
        // 변동비
        private Money variableExpense;
        // 고정비
        private Money fixedExpense;

        public WeeklySales(@NonNull String branchCd, @NonNull YearMonth yearMonth, @NonNull WeekNumber weekNumber) {
            this.branchCd = branchCd;
            this.yearMonth = yearMonth;
            this.weekNumber = weekNumber;
            this.salesAvg = new Money(0L);
            this.expectedTotalSales = new Money(0L);
            this.totalExpense = new Money(0L);
            this.humanResourceExpense = new Money(0L);
            this.variableExpense = new Money(0L);
            this.fixedExpense = new Money(0L);
        }
    }

}
