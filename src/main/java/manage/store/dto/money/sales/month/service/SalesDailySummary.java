package manage.store.dto.money.sales.month.service;

import lombok.Getter;
import lombok.ToString;
import manage.store.exception.common.InvalidParameterException;
import manage.store.model.common.value.RegistDate;
import manage.store.model.money.sales.value.Money;
import org.springframework.util.StringUtils;

@Getter
@ToString
public class SalesDailySummary {

    private final String branchCd;
    private final RegistDate registDate;
    private Money weeklyTotalSales;
    private Money monthTotalSales;

    /**
     * 미래의 날짜에 대한 매출 요약 객체 생성
     * @param branchCd 지점 코드
     * @param registDate 매출 날짜
     * @throws InvalidParameterException 지점 코드나 매출 날짜가 없을 경우
     */
    public SalesDailySummary(String branchCd, RegistDate registDate) {
        if(!StringUtils.hasText(branchCd) || registDate == null) {
            throw new InvalidParameterException("Invalid parameters for SalesSummary. Branch code: " + branchCd + ", Regist date: " + registDate);
        }

        this.branchCd = branchCd;
        this.registDate = registDate;
        this.weeklyTotalSales = new Money(0L);
        this.monthTotalSales = new Money(0L);
    }

    /**
     * 실제 매출 요약이 있는 날짜에 대한 객체 생성
     * @param branchCd 지점 코드
     * @param registDate 매출 날짜
     * @param weeklyTotalSales 주간 총 매출
     * @param monthTotalSales 월간 총 매출
     * @throws InvalidParameterException 지점 코드나 매출 날짜가 없을 경우
     */
    public SalesDailySummary(String branchCd, RegistDate registDate, Money weeklyTotalSales, Money monthTotalSales) {
        this(branchCd, registDate);
        this.weeklyTotalSales = weeklyTotalSales;
        this.monthTotalSales = monthTotalSales;
    }

    public SalesDailySummary setWeeklyTotalSales(Money weeklyTotalSales) {
        return new SalesDailySummary(this.branchCd, this.registDate, weeklyTotalSales, this.monthTotalSales);
    }

    public SalesDailySummary setMonthTotalSales(Money monthTotalSales) {
        return new SalesDailySummary(this.branchCd, this.registDate, this.weeklyTotalSales, monthTotalSales);
    }

}
