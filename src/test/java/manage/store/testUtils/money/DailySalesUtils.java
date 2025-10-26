package manage.store.testUtils.money;

import manage.store.dto.money.sales.month.BasicDailySales;
import manage.store.model.common.value.RegistDate;
import manage.store.model.money.sales.value.Money;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DailySalesUtils {

    /**
     * 2023년 5월의 일별 매출 데이터 리스트
     * 일별 카드 / 현금 매출은 일자와 동일
     * Ex) 1일 -> card, cash: 1, total: 2, cardPercentage: 50
     */
    public static final List<BasicDailySales> SALES_2023_05;

    static {
        List<BasicDailySales> salesList = new ArrayList<>();

        LocalDate startDate = LocalDate.of(2023, 5, 1);
        LocalDate endDate = LocalDate.of(2023, 5, 31);
        LocalDate currentDate = startDate;
        String branchCd = "CONST_BRANCH";

        while (!currentDate.isAfter(endDate)) {
            long dayValue = currentDate.getDayOfMonth();

            Money card = new Money(dayValue);
            Money cash = new Money(dayValue);
            Money totalSales = card.add(cash);
            int cardPercentage = (int) ((card.value() * 100) / totalSales.value());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String yyyyMMdd = currentDate.format(formatter);

            RegistDate regDate = new RegistDate(yyyyMMdd);

            BasicDailySales dailySale = new BasicDailySales(
                    branchCd,
                    regDate,
                    card,
                    cash,
                    totalSales,
                    cardPercentage
            );
            salesList.add(dailySale);

            currentDate = currentDate.plusDays(1);
        }

        SALES_2023_05 = Collections.unmodifiableList(salesList);
    }
}
