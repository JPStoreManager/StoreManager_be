package manage.store.dto.money.month;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import manage.store.model.common.value.RegistDate;
import manage.store.model.money.sales.value.Money;

import java.util.List;

@Data
public class GetMonthSalesResponse {

    private List<DailySales> monthlySales;

    @Getter
    @Setter
    @ToString
    public static class DailySales {

        private String branchCd;
        private RegistDate registDate;
        private Long cardSales;
        private Long cashSales;
        private Long totalSales;
        private int cardPercentage;
        private Long weeklyTotalSales;
        private Long monthTotalSales;
        private Long expense;
        private Long monthTotalExpense;
        private Long variableExpense;
        private String comment;

        public void setCardSales(Long cardSales) {
            this.cardSales = cardSales != null ? cardSales : 0L;
        }
        public void setCardSales(Money cardSales) {
            this.cardSales = cardSales != null ? cardSales.value() : 0L;
        }

        public void setCashSales(Long cashSales) {
            this.cashSales = cashSales != null ? cashSales : 0L;
        }
        public void setCashSales(Money cashSales) {
            this.cashSales = cashSales != null ? cashSales.value() : 0L;
        }

        public void setTotalSales(Long totalSales) {
            this.totalSales = totalSales != null ? totalSales : 0L;
        }
        public void setTotalSales(Money totalSales) {
            this.totalSales = totalSales != null ? totalSales.value() : 0L;
        }

        public void setCardPercentage(int cardPercentage) {
            this.cardPercentage = cardPercentage >= 0 ? cardPercentage : 0;
        }

        public void setWeeklyTotalSales(Long weeklyTotalSales) {
            this.weeklyTotalSales = weeklyTotalSales != null ? weeklyTotalSales : 0L;
        }
        public void setWeeklyTotalSales(Money weeklyTotalSales) {
            this.weeklyTotalSales = weeklyTotalSales != null ? weeklyTotalSales.value() : 0L;
        }

        public void setMonthTotalSales(Long monthTotalSales) {
            this.monthTotalSales = monthTotalSales != null ? monthTotalSales : 0L;
        }
        public void setMonthTotalSales(Money monthTotalSales) {
            this.monthTotalSales = monthTotalSales != null ? monthTotalSales.value() : 0L;
        }

        public void setExpense(Long expense) {
            this.expense = expense != null ? expense : 0L;
        }
        public void setExpense(Money expense) {
            this.expense = expense != null ? expense.value() : 0L;
        }

        public void setMonthTotalExpense(Long monthTotalExpense) {
            this.monthTotalExpense = monthTotalExpense != null ? monthTotalExpense : 0L;
        }
        public void setMonthTotalExpense(Money monthTotalExpense) {
            this.monthTotalExpense = monthTotalExpense != null ? monthTotalExpense.value() : 0L;
        }

        public void setVariableExpense(Long variableExpense) {
            this.variableExpense = variableExpense != null ? variableExpense : 0L;
        }
        public void setVariableExpense(Money variableExpense) {
            this.variableExpense = variableExpense != null ? variableExpense.value() : 0L;
        }

        public DailySales() {
            this.cardSales = 0L;
            this.cashSales = 0L;
            this.totalSales = 0L;
            this.cardPercentage = 0;
            this.weeklyTotalSales = 0L;
            this.monthTotalSales = 0L;
            this.expense = 0L;
            this.monthTotalExpense = 0L;
            this.variableExpense = 0L;
        }

    }

}
