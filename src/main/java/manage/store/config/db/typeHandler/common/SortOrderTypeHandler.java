package manage.store.config.db.typeHandler.common;

import lombok.NoArgsConstructor;
import manage.store.model.common.value.SortOrder;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
@MappedTypes(SortOrder.class)
public class SortOrderTypeHandler extends BaseTypeHandler<SortOrder> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, SortOrder parameter, JdbcType jdbcType) throws SQLException {
        ps.setLong(i, parameter.value());
    }

    @Override
    public SortOrder getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Long value = rs.getLong(columnName);
        if (value == null || value < 0) {
            value = 0L;
        }
        return new SortOrder(value);
    }

    @Override
    public SortOrder getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Long value = rs.getLong(columnIndex);
        if (value == null || value < 0) {
            value = 0L;
        }
        return new SortOrder(value);
    }

    @Override
    public SortOrder getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Long value = cs.getLong(columnIndex);
        if (value == null || value < 0) {
            value = 0L;
        }
        return new SortOrder(value);
    }
}