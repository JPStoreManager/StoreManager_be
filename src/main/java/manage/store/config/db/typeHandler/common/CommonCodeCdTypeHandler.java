package manage.store.config.db.typeHandler.common;
import lombok.NoArgsConstructor;
import manage.store.model.common.value.CommonCodeCd;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
@MappedTypes(CommonCodeCd.class)  // 이 TypeHandler가 처리할 Java 타입 지정
public class CommonCodeCdTypeHandler extends BaseTypeHandler<CommonCodeCd> {

    /**
     * Sets a non-null CommonCodeCd parameter in a PreparedStatement.
     * Converts the value object to its internal String value.
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, CommonCodeCd parameter, JdbcType jdbcType) throws SQLException {
        // CommonCodeCd의 value를 String으로 추출하여 설정
        ps.setString(i, parameter.value());
    }

    /**
     * Retrieves a nullable CommonCodeCd from a ResultSet by column name.
     * If the value is null or empty, returns null; otherwise, creates a new CommonCodeCd instance.
     */
    @Override
    public CommonCodeCd getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        if (value == null || value.isEmpty()) {
            return null;  // null 또는 빈 문자열은 null로 처리 (비즈니스 로직에 따라 조정 가능)
        }
        return new CommonCodeCd(value);  // 유효성 검사는 생성자에서 처리됨
    }

    /**
     * Retrieves a nullable CommonCodeCd from a ResultSet by column index.
     * Similar to the column name version.
     */
    @Override
    public CommonCodeCd getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return new CommonCodeCd(value);
    }

    /**
     * Retrieves a nullable CommonCodeCd from a CallableStatement by column index.
     * Used for stored procedures.
     */
    @Override
    public CommonCodeCd getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return new CommonCodeCd(value);
    }

}