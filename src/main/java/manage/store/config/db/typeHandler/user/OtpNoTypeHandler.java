package manage.store.config.db.typeHandler.user;

import lombok.NoArgsConstructor;
import manage.store.model.user.value.OtpNo;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
@MappedTypes(OtpNo.class)
public class OtpNoTypeHandler extends BaseTypeHandler<OtpNo> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, OtpNo parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.value());
    }

    @Override
    public OtpNo getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return new OtpNo(value);
    }

    @Override
    public OtpNo getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return new OtpNo(value);
    }

    @Override
    public OtpNo getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return new OtpNo(value);
    }
}
