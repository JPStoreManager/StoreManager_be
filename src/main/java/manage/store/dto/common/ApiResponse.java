package manage.store.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;
import manage.store.model.common.value.SuccessFlag;

import java.time.LocalDateTime;

@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private SuccessFlag result;
    private String msg;
    private LocalDateTime timestamp;
    private T data;

    private ApiResponse(SuccessFlag successFlag, String msg, T data) {
        this.result = successFlag;
        this.data = data;
        this.msg = msg;
        this.timestamp = LocalDateTime.now();
    }

    private ApiResponse(SuccessFlag successFlag, String msg) {
        this.result = successFlag;
        this.msg = msg;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Api 성공에 대한 응답 생성
     * @param data 응답 데이터. nullable
     * @param msg 응답 메시지. nullable
     * @return ApiResponse<T> 성공 응답
     * @param <T> 응답 데이터 타입
     */
    public static <T> ApiResponse<T> success(T data, String msg) {
        return new ApiResponse<T>(SuccessFlag.Y, msg, data);
    }

    /**
     * Api 성공에 대한 응답 생성
     * @param msg 응답 메시지. nullable
     * @return ApiResponse<T> 성공 응답
     */
    public static ApiResponse success(String msg) {
        return new ApiResponse(SuccessFlag.Y, msg, null);
    }

    /**
     * Api 실패에 대한 응답 생성
     * @param msg 응답 메시지. nullable
     * @return ApiResponse<T> 실패 응답
     */
    public static ApiResponse fail(String msg) {
        return new ApiResponse(SuccessFlag.N, msg);
    }

}
