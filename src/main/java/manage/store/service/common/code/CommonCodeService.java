package manage.store.service.common.code;


import manage.store.model.common.commonCode.CommonCode;
import manage.store.model.common.value.CommonCodeCd;
import manage.store.model.common.value.CommonCodeGrpCd;
import manage.store.model.common.value.UseYn;

import java.util.List;

/**
 * 공통 코드 서비스 인터페이스
 * <p>
 * 공통 코드의 CRUD 기능을 제공하는 서비스 인터페이스입니다.
 * </p>
 */
public interface CommonCodeService {

    /**
     * 현재 사용되고 있는 공통 코드 리스트 조회
     * @param commonGrpCd 공통 코드의 그룹 코드
     * @return UseYn이 Y인 공통 코드 리스트
     */
    List<CommonCode> getCommonCodes(CommonCodeGrpCd commonGrpCd);

    /**
     * 공통 코드 리스트 조회
     * @param commonGrpCd 공통 코드의 그룹 코드
     * @param useYn 공통 코드의 사용 여부
     * @return 공통 코드 리스트
     */
    List<CommonCode> getCommonCodes(CommonCodeGrpCd commonGrpCd, UseYn useYn);

    /**
     * 현재 사용되고 있는 공통 코드 조회
     * @param commonGrpCd 공통 코드의 그룹 코드
     * @param cd 공통 코드
     * @return UseYn이 Y인 공통 코드
     */
    CommonCode getCommonCode(CommonCodeGrpCd commonGrpCd, CommonCodeCd cd);

    /**
     * 공통 코드 조회
     * @param commonGrpCd 공통 코드의 그룹 코드
     * @param cd 공통 코드
     * @param useYn 공통 코드의 사용 여부
     * @return 공통 코드
     */
    CommonCode getCommonCode(CommonCodeGrpCd commonGrpCd, CommonCodeCd cd, UseYn useYn);

}
