package manage.store.repository.money.mapper;

import manage.store.model.money.sales.DailySales.StoreSales;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SalesMapper {

    List<StoreSales> selectByYear(@Param(value="branchCd") String branchCd,
                                  @Param(value="year") Integer year);

    List<StoreSales> selectByMonth(@Param(value="branchCd") String branchCd,
                                   @Param(value="year") Integer year,
                                   @Param(value="month") Integer month);

    int insert(StoreSales sales);

    int update(StoreSales sales);

}
