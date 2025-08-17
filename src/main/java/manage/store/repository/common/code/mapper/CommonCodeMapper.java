package manage.store.repository.common.code.mapper;

import manage.store.model.common.commonCode.CommonCode;
import manage.store.model.common.value.UseYn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommonCodeMapper {

    List<CommonCode> selectCommonCds(@Param(value = "grpCd") String grpCd,
                                     @Param(value = "useYn") UseYn useYn);

    CommonCode selectCommonCd(@Param(value = "grpCd") String grpCd,
                              @Param(value = "cd")    String cd,
                              @Param(value = "useYn") UseYn useYn);

}
