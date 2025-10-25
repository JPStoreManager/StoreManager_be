package manage.store.repository.money.mapper;

import manage.store.config.db.DBConfiguration;
import manage.store.consts.Profiles;
import manage.store.consts.Tags;
import manage.store.model.common.value.RegistDate;
import manage.store.model.money.sales.DailySales.StoreSales;
import manage.store.model.money.sales.value.Money;
import manage.store.model.user.value.UserId;
import manage.store.testUtils.common.StoreBranchTestUtils;
import manage.store.testUtils.money.SalesUtils;
import manage.store.testUtils.util.BaseDockerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Tag(Tags.Test.UNIT)
//@Testcontainers
@MybatisTest
@ActiveProfiles(Profiles.TEST)
@ContextConfiguration(classes = DBConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StoreSalesMapperTest extends BaseDockerTest {

    @Autowired
    private SalesMapper salesMapper;

    private StoreSales[] salesList;

    @BeforeEach
    public void setUp() {
        final String branchCd = StoreBranchTestUtils.DUMMY_BRANCH1.getBranchCd();
        salesList = new StoreSales[3];
        for (int i = 1; i <= salesList.length; i++) {
            StoreSales sales = SalesUtils.createSales(branchCd, "2010-01-0" + i, new UserId("system"));
            sales.setBranchCd(branchCd);

            salesList[i - 1] = sales;
            salesMapper.insert(sales); // 매출 정보 등록
        }
    }

    /** selectByYear */
    @Test
    @DisplayName("selectByYear 성공")
    void selectByYear_success() {
        // Given
        final String branchCd = salesList[0].getBranchCd();
        final String year = salesList[0].getRegistDate().value().substring(0, 4);

        // When
        final List<StoreSales> result = salesMapper.selectByYear(branchCd, Integer.parseInt(year));

        // Then
        assertThat(result).isNotEmpty();

        final StoreSales dbSales = result.stream()
                .filter(s -> s.getRegistDate().equals(salesList[0].getRegistDate()))
                .findFirst()
                .orElse(null);
        assertNotNull(dbSales);
        SalesUtils.assertSales(salesList[0], dbSales);
    }

    @Test
    @DisplayName("selectByYear 실패 - 데이터 없음")
    void selectByYear_fail_noData() {
        // Given
        final StoreSales notExistSales = SalesUtils.getNotExistSales();
        final String branchCd = notExistSales.getBranchCd();
        final String year = notExistSales.getRegistDate().value().substring(0, 4);

        // When
        final List<StoreSales> result = salesMapper.selectByYear(branchCd, Integer.parseInt(year));

        // Then
        assertThat(result).isEmpty();
    }

    /** selectByMonth */
    @Test
    @DisplayName("selectByMonth 성공")
    void selectByMonth_success() {
        // Given
        final String branchCd = salesList[0].getBranchCd();
        final Integer year = Integer.parseInt(salesList[0].getRegistDate().value().substring(0, 4));
        final Integer month = Integer.parseInt(salesList[0].getRegistDate().value().substring(5, 7));

        // When
        final List<StoreSales> result = salesMapper.selectByMonth(branchCd, year, month);

        // Then
        assertThat(result).isNotEmpty();
        final StoreSales dbSales = result.stream()
                .filter(s -> s.getRegistDate().equals(salesList[0].getRegistDate()))
                .findFirst()
                .orElse(null);

        assertNotNull(dbSales);
        SalesUtils.assertSales(salesList[0], dbSales);
    }

    @Test
    @DisplayName("selectByMonth 실패 - 데이터 없음")
    void selectByMonth_fail_noData() {
        // Given
        final StoreSales notExistSales = SalesUtils.getNotExistSales();
        final String branchCd = notExistSales.getBranchCd();
        final Integer year = Integer.parseInt(notExistSales.getRegistDate().value().substring(0, 4));
        final Integer month = Integer.parseInt(notExistSales.getRegistDate().value().substring(5, 7));

        // When
        final List<StoreSales> result = salesMapper.selectByMonth(branchCd, year, month);

        // Then
        assertThat(result).isEmpty();
    }

    /** insert */
    @Test
    @DisplayName("insert 성공")
    void insert_success() {
        // Given
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String tomorrowDate = sdf.format(calendar.getTime());

        final StoreSales sales = SalesUtils.createSales(StoreBranchTestUtils.DUMMY_BRANCH1.getBranchCd(), tomorrowDate, salesList[0].getCreatedBy());

        // When
        final int result = salesMapper.insert(sales);

        // Then
        assertEquals(1, result);

        final int year = Integer.parseInt(sales.getRegistDate().value().substring(0, 4));
        final int month = Integer.parseInt(sales.getRegistDate().value().substring(5, 7));
        final StoreSales dbSales = salesMapper.selectByMonth(sales.getBranchCd(), year, month).stream()
                .filter(s -> s.getRegistDate().equals(new RegistDate(tomorrowDate)))
                .findFirst()
                .orElse(null);
        SalesUtils.assertSales(sales, dbSales);
    }

    @Test
    @DisplayName("insert 실패 - 중복 데이터(PK)")
    void insert_fail_duplicate() {
        // Given
        final StoreSales sales = salesList[0];

        // When - Then
        assertThrows(DuplicateKeyException.class, () -> salesMapper.insert(sales));
    }

    /** update */
    @Test
    @DisplayName("update 성공")
    void update_success() {
        // Given
        final StoreSales expected = salesList[0];
        expected.setCardSales(new Money(expected.getCardSales().value() + 99999L));
        expected.setCashSales(new Money(expected.getCashSales().value() + 99999L));
        expected.setComment("Updated comment");
        expected.setLastUpdatedBy(expected.getCreatedBy());

        // When
        final int result = salesMapper.update(expected);

        // Then
        assertEquals(1, result);

        final int year = Integer.parseInt(expected.getRegistDate().value().substring(0, 4));
        final int month = Integer.parseInt(expected.getRegistDate().value().substring(5, 7));
        final StoreSales actual = salesMapper.selectByMonth(expected.getBranchCd(), year, month).stream()
                .filter(s -> s.getRegistDate().equals(expected.getRegistDate()))
                .findFirst().orElse(null);

        SalesUtils.assertSales(expected, actual);
    }

    @Test
    @DisplayName("update 실패 - 존재하지 않는 데이터")
    void update_fail_noData() {
        // Given
        final StoreSales sales = SalesUtils.createSales("noBranch", "1999-01-01", new UserId("testUser"));

        // When
        final int result = salesMapper.update(sales);

        // Then
        assertEquals(0, result);
    }
}

