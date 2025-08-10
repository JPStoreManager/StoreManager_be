package manage.store.config.db.typeHandler.user;

import lombok.NoArgsConstructor;
import manage.store.model.user.value.ResidentRegistNo;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
@MappedTypes(ResidentRegistNo.class)
public class ResidentRegistNoTypeHandler extends BaseTypeHandler<ResidentRegistNo> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ResidentRegistNo parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.value());
    }

    @Override
    public ResidentRegistNo getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return new ResidentRegistNo(value);
    }

    @Override
    public ResidentRegistNo getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return new ResidentRegistNo(value);
    }

    @Override
    public ResidentRegistNo getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return new ResidentRegistNo(value);
    }
}
