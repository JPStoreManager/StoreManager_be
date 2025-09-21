package manage.store.model.common.commonCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import manage.store.model.common.value.CommonCodeCd;
import manage.store.model.common.value.CommonCodeGrpCd;
import manage.store.model.common.value.SortOrder;
import manage.store.model.common.value.UseYn;
import manage.store.model.user.value.UserId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonCode {

    private CommonCodeGrpCd grpCd;
    private String grpCdDesc;
    private CommonCodeCd cd;
    private String cdVal;
    private String cdDesc;
    private UseYn useYn;
    private SortOrder sortOrder;
    private UserId createdBy;
    private String createdDate;
    private UserId lastUpdatedBy;
    private String lastUpdatedDate;

}
