package manage.store.config.db.typeHandler.sales;

import lombok.NoArgsConstructor;
import manage.store.model.money.sales.value.Money;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@NoArgsConstructor
@MappedTypes(Money.class)
public class MoneyTypeHandler extends BaseTypeHandler<Money> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Money parameter, JdbcType jdbcType) throws SQLException {
        ps.setLong(i, parameter.value());
    }

    @Override
    public Money getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Long value = rs.getLong(columnName);
        if (value == null || value < 0) {
            value = 0L;
        }
        return new Money(value);
    }

    @Override
    public Money getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Long value = rs.getLong(columnIndex);
        if (value == null || value < 0) {
            value = 0L;
        }
        return new Money(value);
    }

    @Override
    public Money getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Long value = cs.getLong(columnIndex);
        if (value == null || value < 0) {
            value = 0L;
        }
        return new Money(value);
    }
}
