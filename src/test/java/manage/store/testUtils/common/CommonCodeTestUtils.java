package manage.store.testUtils.common;


import manage.store.model.common.commonCode.CommonCode;
import manage.store.model.common.value.CommonCodeCd;
import manage.store.model.common.value.CommonCodeGrpCd;
import manage.store.model.common.value.SortOrder;
import manage.store.model.common.value.UseYn;

public interface CommonCodeTestUtils {

    CommonCodeGrpCd TEST_GRP_CD = new CommonCodeGrpCd("TEST_GRP_CD");

    CommonCode COMMON_CODE1 = CommonCode.builder()
            .grpCd(TEST_GRP_CD)
            .grpCdDesc("test group code")
            .cd(new CommonCodeCd("CD1"))
            .cdVal("CDVAL1")
            .cdDesc("code value desc")
            .useYn(UseYn.Y)
            .sortOrder(new SortOrder(1L))
            .createdBy("system")
            .createdDate("2025-07-26 00:00:00.000")
            .lastUpdatedBy("system")
            .lastUpdatedDate("2025-07-26 00:00:00.000")
            .build();

    CommonCode COMMON_CODE2 = CommonCode.builder()
            .grpCd(TEST_GRP_CD)
            .grpCdDesc("test group code")
            .cd(new CommonCodeCd("CD2"))
            .cdVal("CDVAL2")
            .cdDesc("code value desc")
            .useYn(UseYn.Y)
            .sortOrder(new SortOrder(2L))
            .createdBy("system")
            .createdDate("2025-07-26 00:00:00.000")
            .lastUpdatedBy("system")
            .lastUpdatedDate("2025-07-26 00:00:00.000")
            .build();

    CommonCode COMMON_CODE3 = CommonCode.builder()
            .grpCd(TEST_GRP_CD)
            .grpCdDesc("test group code")
            .cd(new CommonCodeCd("CD3"))
            .cdVal("CDVAL3")
            .cdDesc("code value desc")
            .useYn(UseYn.Y)
            .sortOrder(new SortOrder(3L))
            .createdBy("system")
            .createdDate("2025-07-26 00:00:00.000")
            .lastUpdatedBy("system")
            .lastUpdatedDate("2025-07-26 00:00:00.000")
            .build();

    CommonCode COMMON_CODE4 = CommonCode.builder()
            .grpCd(TEST_GRP_CD)
            .grpCdDesc("test group code")
            .cd(new CommonCodeCd("CD4"))
            .cdVal("CDVAL4")
            .cdDesc("code value desc")
            .useYn(UseYn.N)
            .sortOrder(new SortOrder(4L))
            .createdBy("system")
            .createdDate("2025-07-26 00:00:00.000")
            .lastUpdatedBy("system")
            .lastUpdatedDate("2025-07-26 00:00:00.000")
            .build();

    CommonCode COMMON_CODE5 = CommonCode.builder()
            .grpCd(TEST_GRP_CD)
            .grpCdDesc("test group code")
            .cd(new CommonCodeCd("CD5"))
            .cdVal("CDVAL5")
            .cdDesc("code value desc")
            .useYn(UseYn.N)
            .sortOrder(new SortOrder(5L))
            .createdBy("system")
            .createdDate("2025-07-26 00:00:00.000")
            .lastUpdatedBy("system")
            .lastUpdatedDate("2025-07-26 00:00:00.000")
            .build();

    CommonCode[] USE_COMMON_CODES = {COMMON_CODE1, COMMON_CODE2, COMMON_CODE3};
    CommonCode[] NOT_USE_COMMON_CODES = {COMMON_CODE4, COMMON_CODE5};
}