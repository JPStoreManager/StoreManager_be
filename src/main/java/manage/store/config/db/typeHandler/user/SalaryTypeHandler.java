package manage.store.config.db.typeHandler.user;

import lombok.NoArgsConstructor;
import manage.store.model.user.value.Salary;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
@MappedTypes(Salary.class)
public class SalaryTypeHandler extends BaseTypeHandler<Salary> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Salary parameter, JdbcType jdbcType) throws SQLException {
        ps.setLong(i, parameter.value());
    }

    @Override
    public Salary getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Long value = rs.getLong(columnName);
        if (value == null || value < 0) {
            value = 0L;
        }
        return new Salary(value);
    }

    @Override
    public Salary getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Long value = rs.getLong(columnIndex);
        if (value == null || value < 0) {
            value = 0L;
        }
        return new Salary(value);
    }

    @Override
    public Salary getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Long value = cs.getLong(columnIndex);
        if (value == null || value < 0) {
            value = 0L;
        }
        return new Salary(value);
    }
}
