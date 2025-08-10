package manage.store.config.db.typeHandler.common;

import lombok.NoArgsConstructor;
import manage.store.model.common.value.DbUpdateDate;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@MappedTypes(DbUpdateDate.class)
public class DbUpdateDateTypeHandler extends BaseTypeHandler<DbUpdateDate> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, DbUpdateDate parameter, JdbcType jdbcType) throws SQLException {
        ps.setTimestamp(i, Timestamp.valueOf(parameter.value()));
    }

    @Override
    public DbUpdateDate getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnName);
        return toDbUpdateDate(timestamp);
    }

    @Override
    public DbUpdateDate getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnIndex);
        return toDbUpdateDate(timestamp);
    }

    @Override
    public DbUpdateDate getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Timestamp timestamp = cs.getTimestamp(columnIndex);
        return toDbUpdateDate(timestamp);
    }

    private DbUpdateDate toDbUpdateDate(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        // Timestamp를 LocalDateTime으로 변환하여 DbUpdateDate 객체를 생성합니다.
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        return new DbUpdateDate(localDateTime);
    }
}