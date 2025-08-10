package manage.store.config.db.typeHandler.user;

import lombok.NoArgsConstructor;
import manage.store.model.user.value.WorkDate;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.time.LocalDate;

@NoArgsConstructor
@MappedTypes(WorkDate.class)
public class WorkDateTypeHandler extends BaseTypeHandler<WorkDate> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, WorkDate parameter, JdbcType jdbcType) throws SQLException {
        ps.setDate(i, Date.valueOf(parameter.value()));
    }

    @Override
    public WorkDate getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Date date = rs.getDate(columnName);
        return toWorkDate(date);
    }

    @Override
    public WorkDate getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Date date = rs.getDate(columnIndex);
        return toWorkDate(date);
    }

    @Override
    public WorkDate getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Date date = cs.getDate(columnIndex);
        return toWorkDate(date);
    }

    private WorkDate toWorkDate(Date date) {
        if (date == null) {
            return null;
        }
        LocalDate localDate = date.toLocalDate();
        return new WorkDate(localDate);
    }
}