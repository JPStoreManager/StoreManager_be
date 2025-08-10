package manage.store.config.db.typeHandler.common;

import lombok.NoArgsConstructor;
import manage.store.model.common.value.DeleteFlag;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
@MappedTypes(DeleteFlag.class)
public class DeleteFlagTypeHandler extends BaseTypeHandler<DeleteFlag> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, DeleteFlag parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.value());
    }

    @Override
    public DeleteFlag getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return DeleteFlag.from(value);
    }

    @Override
    public DeleteFlag getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return DeleteFlag.from(value);
    }

    @Override
    public DeleteFlag getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return DeleteFlag.from(value);
    }
}

