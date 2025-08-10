package manage.store.config.db.typeHandler.common;

import lombok.NoArgsConstructor;
import manage.store.model.common.value.CommonCodeGrpCd;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
@MappedTypes(CommonCodeGrpCd.class)
public class CommonCodeGrpCdTypeHandler extends BaseTypeHandler<CommonCodeGrpCd> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, CommonCodeGrpCd parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.value());
    }

    @Override
    public CommonCodeGrpCd getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return new CommonCodeGrpCd(value);
    }

    @Override
    public CommonCodeGrpCd getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return new CommonCodeGrpCd(value);
    }

    @Override
    public CommonCodeGrpCd getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return new CommonCodeGrpCd(value);
    }
}
