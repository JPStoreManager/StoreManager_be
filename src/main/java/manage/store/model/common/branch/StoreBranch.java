package manage.store.model.common.branch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import manage.store.model.common.value.SortOrder;
import manage.store.model.common.value.UseYn;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreBranch {

    private String branchCd;
    private String branchNm;
    private String branchDesc;
    private String address;
    private UseYn useYn;
    private SortOrder sortOrder;
    private String createdBy;
    private String createdDate;
    private String lastUpdatedBy;
    private String lastUpdatedDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StoreBranch that = (StoreBranch) o;

        return branchCd != null ? branchCd.equals(that.branchCd) : that.branchCd == null;
    }

}
