package manage.store.dto.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import manage.store.model.common.value.SuccessFlag;

import java.time.LocalDateTime;

@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final SuccessFlag result;
    private final String msg;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;
    private T data;

    @JsonCreator
    // For Test
    private ApiResponse(
            @JsonProperty("result") SuccessFlag result,
            @JsonProperty("msg") String msg,
            @JsonProperty("data") T data,
            @JsonProperty("timestamp") LocalDateTime timestamp
    ) {
        this.result = result;
        this.data = data;
        this.msg = msg;
        this.timestamp = timestamp;
    }

    private ApiResponse(
            @JsonProperty("result") SuccessFlag result,
            @JsonProperty("msg") String msg,
            @JsonProperty("data") T data
    ) {
        this.result = result;
        this.data = data;
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
    public static <T> ApiResponse<T> success(String msg) {
        return new ApiResponse<T>(SuccessFlag.Y, msg, null);
    }

    /**
     * Api 실패에 대한 응답 생성
     * @param msg 응답 메시지. nullable
     * @return ApiResponse<T> 실패 응답
     */
    public static <T> ApiResponse<T> fail(String msg) {
        return new ApiResponse<T>(SuccessFlag.N, msg, null);
    }

}
