package manage.store.repository;

import lombok.extern.slf4j.Slf4j;
import manage.store.exception.common.db.DbOperDataAccessException;
import manage.store.exception.common.db.DbOperNonTransientException;
import manage.store.exception.common.db.DbOperOtherException;
import manage.store.exception.common.db.DbOperTransientException;
import manage.store.utils.ExceptionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.dao.TransientDataAccessException;

@Slf4j
public class BaseRepository {
    /**
     * 쿼리 실행 시 발생하는 익셉션을 catch하고 wrap하여 던지는 역할 수행
     */
    protected void handleAndWrapException(Exception e, String methodName, Object param) throws DbOperNonTransientException, DbOperTransientException, DbOperDataAccessException, DbOperOtherException{
        String errorMsg = ExceptionUtils.getExceptionErrorMsg(e);
        String logMsg = String.format("Fail in %s. Param: %s, Error: %s", methodName, param, errorMsg);
        log.error(logMsg);

        if (e instanceof NonTransientDataAccessException) {
            // 2. NonTransient 유형 wrapping
            throw new DbOperNonTransientException("Non-transient data access error in " + methodName + ": " + errorMsg, e);
        } else if (e instanceof TransientDataAccessException) {
            // 3. Transient 유형 wrapping
            throw new DbOperTransientException("Transient data access error in " + methodName + ": " + errorMsg, e);
        } else if (e instanceof DataAccessException) {
            // 1. DataAccessException 상위 유형 wrapping (Non/Transient 제외)
            throw new DbOperDataAccessException("General data access error in " + methodName + ": " + errorMsg, e);
        } else {
            // 4. 기타 (SQLException 등) wrapping
            throw new DbOperOtherException("Other exception in " + methodName + ": " + errorMsg, e);
        }
    }

}
