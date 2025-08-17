package manage.store.service.common;

import manage.store.exception.common.InvalidParameterException;
import manage.store.model.common.commonCode.CommonCode;
import manage.store.model.common.value.CommonCodeCd;
import manage.store.model.common.value.CommonCodeGrpCd;
import manage.store.model.common.value.UseYn;
import manage.store.repository.common.code.mapper.CommonCodeMapper;
import manage.store.service.common.code.CommonCodeServiceImpl;
import manage.store.testUtils.common.CommonCodeTestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommonCodeServiceImplTest {

    @Mock
    private CommonCodeMapper commonCodeMapper;

    @InjectMocks
    private CommonCodeServiceImpl commonCodeService;

    @Test
    @DisplayName("getCommonCodes 성공 - 사용중인 공통 코드")
    void getCommonCodes_success_UsableCommonCode() {
        // Given
        CommonCodeGrpCd grpCd = CommonCodeTestUtils.TEST_GRP_CD;
        UseYn useYn = UseYn.Y;
        given(commonCodeMapper.selectCommonCds(grpCd.value(), useYn))
                .willReturn(List.of(CommonCodeTestUtils.USE_COMMON_CODES));

        // When
        List<CommonCode> actual = commonCodeService.getCommonCodes(grpCd, useYn);

        // Then
        assertNotNull(actual);
        verify(commonCodeMapper).selectCommonCds(grpCd.value(), useYn);
        Assertions.assertThat(actual.size()).isEqualTo(CommonCodeTestUtils.USE_COMMON_CODES.length);
    }

    @Test
    @DisplayName("getCommonCodes 성공 - 사용중이지 않은 공통 코드")
    void getCommonCodes_success_NonUsableCommonCode() {
        // Given
        CommonCodeGrpCd grpCd = CommonCodeTestUtils.TEST_GRP_CD;
        UseYn useYn = UseYn.N;
        given(commonCodeMapper.selectCommonCds(grpCd.value(), useYn))
                .willReturn(List.of(CommonCodeTestUtils.NOT_USE_COMMON_CODES));

        // When
        List<CommonCode> actual = commonCodeService.getCommonCodes(grpCd, useYn);

        // Then
        assertNotNull(actual);
        verify(commonCodeMapper).selectCommonCds(grpCd.value(), useYn);
        Assertions.assertThat(actual.size()).isEqualTo(CommonCodeTestUtils.NOT_USE_COMMON_CODES.length);
    }

    @Test
    @DisplayName("getCommonCodes 성공 - useYn 기본값")
    void getCommonCodes_success_defaultUseYn() {
        // Given
        CommonCodeGrpCd grpCd = CommonCodeTestUtils.TEST_GRP_CD;
        given(commonCodeMapper.selectCommonCds(grpCd.value(), UseYn.Y))
                .willReturn(List.of(CommonCodeTestUtils.USE_COMMON_CODES));

        // When
        List<CommonCode> actual = commonCodeService.getCommonCodes(grpCd);

        // Then
        assertNotNull(actual);
        verify(commonCodeMapper).selectCommonCds(grpCd.value(), UseYn.Y);
        Assertions.assertThat(actual.size()).isEqualTo(CommonCodeTestUtils.USE_COMMON_CODES.length);
    }

    @Test
    @DisplayName("getCommonCodes 실패 - 유효하지 않은 파라미터")
    void getCommonCodes_fail_invalidParameters() {
        // When & Then
        assertThrows(InvalidParameterException.class, () -> commonCodeService.getCommonCodes(null, UseYn.Y));
        assertThrows(InvalidParameterException.class, () -> commonCodeService.getCommonCodes(CommonCodeTestUtils.TEST_GRP_CD, null));
        assertThrows(InvalidParameterException.class, () -> commonCodeService.getCommonCodes(null, null));
        assertThrows(InvalidParameterException.class, () -> commonCodeService.getCommonCodes(null));
    }


    @Test
    @DisplayName("getCommonCode 성공 - 사용중인 공통 코드")
    void getCommonCode_success_UsableCommonCode() {
        // Given
        CommonCodeGrpCd grpCd = CommonCodeTestUtils.COMMON_CODE1.getGrpCd();
        CommonCodeCd cd = CommonCodeTestUtils.COMMON_CODE1.getCd();
        UseYn useYn = UseYn.Y;
        given(commonCodeMapper.selectCommonCd(grpCd.value(), cd.value(), useYn))
                .willReturn(CommonCodeTestUtils.COMMON_CODE1);

        // When
        CommonCode actual = commonCodeService.getCommonCode(grpCd, cd, useYn);

        // Then
        assertNotNull(actual);
        verify(commonCodeMapper).selectCommonCd(grpCd.value(), cd.value(), useYn);
        assertEquals(CommonCodeTestUtils.COMMON_CODE1, actual);
    }

    @Test
    @DisplayName("getCommonCode 성공 - 사용중이지 않은 공통 코드")
    void getCommonCode_success_NonUsableCommonCode() {
        // Given
        CommonCodeGrpCd grpCd = CommonCodeTestUtils.COMMON_CODE4.getGrpCd();
        CommonCodeCd cd = CommonCodeTestUtils.COMMON_CODE4.getCd();
        UseYn useYn = UseYn.N;
        given(commonCodeMapper.selectCommonCd(grpCd.value(), cd.value(), useYn))
                .willReturn(CommonCodeTestUtils.COMMON_CODE4);

        // When
        CommonCode actual = commonCodeService.getCommonCode(grpCd, cd, useYn);

        // Then
        assertNotNull(actual);
        verify(commonCodeMapper).selectCommonCd(grpCd.value(), cd.value(), useYn);
        assertEquals(CommonCodeTestUtils.COMMON_CODE4, actual);
    }

    @Test
    @DisplayName("getCommonCode 성공 - useYn 기본값")
    void getCommonCode_success_defaultUseYn() {
        // Given
        CommonCodeGrpCd grpCd = CommonCodeTestUtils.COMMON_CODE1.getGrpCd();
        CommonCodeCd cd = CommonCodeTestUtils.COMMON_CODE1.getCd();
        given(commonCodeMapper.selectCommonCd(grpCd.value(), cd.value(), UseYn.Y))
                .willReturn(CommonCodeTestUtils.COMMON_CODE1);

        // When
        CommonCode actualCode = commonCodeService.getCommonCode(grpCd, cd);

        // Then
        assertNotNull(actualCode);
        verify(commonCodeMapper).selectCommonCd(grpCd.value(), cd.value(), UseYn.Y);
        assertEquals(CommonCodeTestUtils.COMMON_CODE1, actualCode);
    }

    @Test
    @DisplayName("getCommonCode 실패 - 유효하지 않은 파라미터")
    void getCommonCode_fail_invalidParameters() {
        // When & Then
        assertThrows(InvalidParameterException.class, () -> commonCodeService.getCommonCode(null, CommonCodeTestUtils.COMMON_CODE1.getCd(), UseYn.Y));
        assertThrows(InvalidParameterException.class, () -> commonCodeService.getCommonCode(CommonCodeTestUtils.COMMON_CODE1.getGrpCd(), null, UseYn.Y));
        assertThrows(InvalidParameterException.class, () -> commonCodeService.getCommonCode(CommonCodeTestUtils.COMMON_CODE1.getGrpCd(), CommonCodeTestUtils.COMMON_CODE1.getCd(), null));

        assertThrows(InvalidParameterException.class, () -> commonCodeService.getCommonCode(null, null, UseYn.Y));
        assertThrows(InvalidParameterException.class, () -> commonCodeService.getCommonCode(null, CommonCodeTestUtils.COMMON_CODE1.getCd(), null));
        assertThrows(InvalidParameterException.class, () -> commonCodeService.getCommonCode(CommonCodeTestUtils.COMMON_CODE1.getGrpCd(), null, null));

        assertThrows(InvalidParameterException.class, () -> commonCodeService.getCommonCode(null, null, null));

        assertThrows(InvalidParameterException.class, () -> commonCodeService.getCommonCode(null, CommonCodeTestUtils.COMMON_CODE1.getCd()));
        assertThrows(InvalidParameterException.class, () -> commonCodeService.getCommonCode(CommonCodeTestUtils.COMMON_CODE1.getGrpCd(), null));

        assertThrows(InvalidParameterException.class, () -> commonCodeService.getCommonCode(null, null));
    }
}