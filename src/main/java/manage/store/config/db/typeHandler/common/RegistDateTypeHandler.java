package manage.store.config.db.typeHandler.common;

import lombok.NoArgsConstructor;
import manage.store.model.common.value.RegistDate;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
@MappedTypes(RegistDate.class)
public class RegistDateTypeHandler extends BaseTypeHandler<RegistDate> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, RegistDate parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.value());
    }

    @Override
    public RegistDate getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return new RegistDate(value);
    }

    @Override
    public RegistDate getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return new RegistDate(value);
    }

    @Override
    public RegistDate getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return new RegistDate(value);
    }
}

