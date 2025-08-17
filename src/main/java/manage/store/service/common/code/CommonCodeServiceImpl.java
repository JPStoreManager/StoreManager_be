package manage.store.service.common.code;

import lombok.RequiredArgsConstructor;
import manage.store.repository.common.code.mapper.CommonCodeMapper;
import manage.store.exception.common.InvalidParameterException;
import manage.store.model.common.commonCode.CommonCode;
import manage.store.model.common.value.CommonCodeCd;
import manage.store.model.common.value.CommonCodeGrpCd;
import manage.store.model.common.value.UseYn;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonCodeServiceImpl implements CommonCodeService {

    private final CommonCodeMapper commonCodeMapper;

    @Override
    public List<CommonCode> getCommonCodes(CommonCodeGrpCd commonGrpCd) {
        return getCommonCodes(commonGrpCd, UseYn.Y);
    }

    @Override
    public List<CommonCode> getCommonCodes(CommonCodeGrpCd commonGrpCd, UseYn useYn) {
        if(commonGrpCd == null || useYn == null) throw new InvalidParameterException("CommonCodeGrpCd and UseYn must not be null");

        return commonCodeMapper.selectCommonCds(commonGrpCd.value(), useYn);
    }

    @Override
    public CommonCode getCommonCode(CommonCodeGrpCd commonGrpCd, CommonCodeCd cd) {
        return getCommonCode(commonGrpCd, cd, UseYn.Y);
    }

    @Override
    public CommonCode getCommonCode(CommonCodeGrpCd commonGrpCd, CommonCodeCd cd, UseYn useYn) {
        if(commonGrpCd == null || cd == null || useYn == null) {
            throw new InvalidParameterException("CommonCodeGrpCd, cd and UseYn must not be null");
        }

        return commonCodeMapper.selectCommonCd(commonGrpCd.value(), cd.value(), useYn);
    }
}
