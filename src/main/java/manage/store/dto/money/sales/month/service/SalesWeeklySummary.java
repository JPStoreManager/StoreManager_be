package manage.store.dto.money.sales.month.service;

import lombok.Getter;
import lombok.ToString;
import manage.store.exception.common.InvalidParameterException;
import manage.store.model.common.value.WeekNumber;
import manage.store.model.common.value.YearMonth;
import manage.store.model.money.sales.value.Money;
import org.springframework.util.StringUtils;

@Getter
@ToString
public class SalesWeeklySummary {

    private String branchCd;
    private YearMonth yearMonth;
    private WeekNumber weekNumber;
    // 주간 평균 매출
    private Money salesAvg;
    // 주간 예상 매출 -- 아직 한 주가 다 지나지 않은 경우에 활용
    private Money expectedTotalSales;

    public SalesWeeklySummary(String branchCd, YearMonth yearMonth, WeekNumber weekNumber) {
        if(!StringUtils.hasText(branchCd) || yearMonth == null || weekNumber == null) {
            throw new InvalidParameterException("Invalid parameters for WeeklySalesSummary. Branch code: " + branchCd + ", YearMonth: " + yearMonth + ", WeekNumber: " + weekNumber);
        }

        this.branchCd = branchCd;
        this.yearMonth = yearMonth;
        this.weekNumber = weekNumber;
        this.salesAvg = new Money(0L);
        this.expectedTotalSales = new Money(0L);
    }

    private SalesWeeklySummary(
            String branchCd,
            YearMonth yearMonth,
            WeekNumber weekNumber,
            Money salesAvg,
            Money expectedTotalSales) {
        this(branchCd, yearMonth, weekNumber);
        this.salesAvg = salesAvg;
        this.expectedTotalSales = expectedTotalSales;
    }

    public static class Builder {
        private String branchCd;
        private YearMonth yearMonth;
        private WeekNumber weekNumber;
        private Money salesAvg = new Money(0L);
        private Money expectedTotalSales = new Money(0L);

        /**
         * 필수값 지정을 위한 생성자 사용
         * @param branchCd 지점 코드
         * @param yearMonth 년월
         * @param weekNumber 몇번째 주 이상을 나타내는 숫자
         */
        public Builder(String branchCd, YearMonth yearMonth, WeekNumber weekNumber) {
            this.branchCd = branchCd;
            this.yearMonth = yearMonth;
            this.weekNumber = weekNumber;
        }

        public Builder salesAvg(Money salesAvg) {
            this.salesAvg = salesAvg;
            return this;
        }

        public Builder expectedTotalSales(Money expectedTotalSales) {
            this.expectedTotalSales = expectedTotalSales;
            return this;
        }

        public SalesWeeklySummary build() {
            return new SalesWeeklySummary(
                    branchCd,
                    yearMonth,
                    weekNumber,
                    salesAvg,
                    expectedTotalSales);
        }
    }

    public SalesWeeklySummary setSalesAvg(Money salesAvg) {
        return new SalesWeeklySummary(
                this.branchCd,
                this.yearMonth,
                this.weekNumber,
                salesAvg,
                this.expectedTotalSales);
    }

    public SalesWeeklySummary setExpectedTotalSales(Money expectedTotalSales) {
        return new SalesWeeklySummary(
                this.branchCd,
                this.yearMonth,
                this.weekNumber,
                this.salesAvg,
                expectedTotalSales);
    }

}
