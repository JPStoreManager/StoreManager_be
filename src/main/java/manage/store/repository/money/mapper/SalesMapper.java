package manage.store.repository.money.mapper;

import manage.store.model.money.sales.DailySales.DailySales;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SalesMapper {

    List<DailySales> selectByYear(@Param(value="branchCd") String branchCd,
                                  @Param(value="year") Integer year);

    List<DailySales> selectByMonth(@Param(value="branchCd") String branchCd,
                              @Param(value="year") Integer year,
                              @Param(value="month") Integer month);

    int insert(DailySales sales);

    int update(DailySales sales);

}
