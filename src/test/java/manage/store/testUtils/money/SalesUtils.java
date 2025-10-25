package manage.store.testUtils.money;

import manage.store.model.common.value.DbUpdateDate;
import manage.store.model.common.value.RegistDate;
import manage.store.model.money.sales.DailySales.StoreSales;
import manage.store.model.money.sales.value.Money;
import manage.store.model.user.value.UserId;
import manage.store.testUtils.util.CommonUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public interface SalesUtils {

    /**
     * Sales 객체를 생성한다.
     * @param branchCd 지점 코드
     * @param registDate 등록 날짜 (형식: "yyyy-MM-dd")
     * @param createdBy 생성자
     * @return Sales 객체
     */
    static StoreSales createSales(String branchCd, String registDate, UserId createdBy) {
        final StoreSales sales = new StoreSales();
        sales.setBranchCd(branchCd);
        sales.setRegistDate(new RegistDate(registDate));
        sales.setCardSales(new Money((long) (CommonUtils.getRandomInt() % 10000)));
        sales.setCashSales(new Money((long) (CommonUtils.getRandomInt() % 10000)));
        sales.setCreatedBy(createdBy);
        sales.setCreatedDate(new DbUpdateDate(CommonUtils.getRandomLocalDateTime()));
        sales.setLastUpdatedBy(createdBy);
        sales.setLastUpdatedDate(new DbUpdateDate(CommonUtils.getRandomLocalDateTime()));

        return sales;
    }

    static StoreSales getNotExistSales() {
        final StoreSales sales = new StoreSales();
        sales.setBranchCd("NOT_EXIST");
        sales.setRegistDate(new RegistDate("1999-12-31"));

        return sales;
    }

    static StoreSales clone(StoreSales from) {
        final StoreSales clone = new StoreSales();
        clone.setBranchCd(from.getBranchCd());
        clone.setRegistDate(from.getRegistDate());
        clone.setCardSales(from.getCardSales());
        clone.setCashSales(from.getCashSales());
        clone.setComment(from.getComment());
        clone.setCreatedBy(from.getCreatedBy());
        clone.setCreatedDate(from.getCreatedDate());
        clone.setLastUpdatedBy(from.getLastUpdatedBy());
        clone.setLastUpdatedDate(from.getLastUpdatedDate());

        return clone;
    }

    /**
     * Sales 객체를 비교한다.
     * @param expected 기대하는 Sales 객체
     * @param actual 실제 Sales 객체
     */
    static void assertSales(StoreSales expected, StoreSales actual) {
        assertThat(actual).isNotNull();
        assertThat(actual.getBranchCd()).isEqualTo(expected.getBranchCd());
        assertThat(actual.getRegistDate()).isEqualTo(expected.getRegistDate());
        assertThat(actual.getCardSales()).isEqualTo(expected.getCardSales());
        assertThat(actual.getCashSales()).isEqualTo(expected.getCashSales());
        assertThat(actual.getCreatedBy()).isEqualTo(expected.getCreatedBy());
        assertThat(actual.getLastUpdatedBy()).isEqualTo(expected.getLastUpdatedBy());
    }
}

