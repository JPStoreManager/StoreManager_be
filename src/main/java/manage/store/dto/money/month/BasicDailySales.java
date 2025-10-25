package manage.store.dto.money.month;

import lombok.Getter;
import lombok.ToString;
import manage.store.exception.common.InvalidParameterException;
import manage.store.model.common.value.RegistDate;
import manage.store.model.money.sales.value.Money;
import org.springframework.util.StringUtils;

/**
 * 월별 매출의 날짜별 기본 데이터 객체
 * Setter를 사용하여 데이터를 연속적으로 설정하면 그만큼 많은 객체가 생성됨으로 초기화 시 생성자 사용 권장
 */
@Getter
@ToString
public class BasicDailySales {

    private final String branchCd;
    private final RegistDate registDate;
    private Money cardSales;
    private Money cashSales;
    private Money totalSales;
    private int cardPercentage;

    /**
     * 미래의 날짜에 대한 매출 혹은 매출이 없는 날짜에 대한 객체 생성
     * @param branchCd 지점 코드
     * @param registDate 매출 날짜
     * @throws InvalidParameterException 지점 코드나 매출 날짜가 없을 경우
     */
    public BasicDailySales(String branchCd, RegistDate registDate) {
        if(!StringUtils.hasText(branchCd) || registDate == null) {
            throw new InvalidParameterException("Invalid parameters for BasicSales. Branch code: " + branchCd + ", Regist date: " + registDate);
        }

        this.branchCd = branchCd;
        this.registDate = registDate;
        this.cardSales = new Money(0L);
        this.cashSales = new Money(0L);
        this.totalSales = new Money(0L);
        this.cardPercentage = 0;
    }

    /**
     * 실제 매출이 있는 날짜에 대한 객체 생성
     * @param branchCd 지점 코드
     * @param registDate 매출 날짜
     * @param cardSales 카드 매출
     * @param cashSales 현금 매출
     * @param totalSales 총 매출
     * @param cardPercentage 카드 매출 비율
     * @throws InvalidParameterException 지점 코드나 매출 날짜가 없을 경우
     */
    public BasicDailySales(String branchCd, RegistDate registDate, Money cardSales, Money cashSales, Money totalSales, int cardPercentage) {
        this(branchCd, registDate);
        this.cardSales = cardSales;
        this.cashSales = cashSales;
        this.totalSales = totalSales;
        this.cardPercentage = cardPercentage;
    }

    public BasicDailySales setCardSales(Money cardSales) {
        return new BasicDailySales(this.branchCd, this.registDate, cardSales, this.cashSales, this.totalSales, this.cardPercentage);
    }

    public BasicDailySales setCashSales(Money cashSales) {
        return new BasicDailySales(this.branchCd, this.registDate, this.cardSales, cashSales, this.totalSales, this.cardPercentage);
    }

    public BasicDailySales setTotalSales(Money totalSales) {
        return new BasicDailySales(this.branchCd, this.registDate, this.cardSales, this.cashSales, totalSales, this.cardPercentage);
    }

    public BasicDailySales setCardPercentage(int cardPercentage) {
        return new BasicDailySales(this.branchCd, this.registDate, this.cardSales, this.cashSales, this.totalSales, cardPercentage);
    }

}
