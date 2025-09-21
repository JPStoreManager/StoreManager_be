package manage.store.utils;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GsonUtils {

    /**
     * 기본 설정이 완료된 Gson 객체 생성
     * @return Gson 객체
     */
    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

}

class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    // LocalDateTime을 JSON으로 직렬화할 때 사용할 포맷 정의
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        // LocalDateTime 객체를 지정된 포맷의 문자열로 변환하여 JsonPrimitive로 반환
        return new JsonPrimitive(FORMATTER.format(src));
    }

    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonArray()) {
            JsonArray asJsonArray = json.getAsJsonArray();
            int year = asJsonArray.get(0).getAsInt();
            int month = asJsonArray.get(1).getAsInt();
            int day = asJsonArray.get(2).getAsInt();
            int hour = asJsonArray.get(3).getAsInt();
            int minute = asJsonArray.get(4).getAsInt();
            int second = asJsonArray.get(5).getAsInt();
            // 나노초는 배열 크기에 따라 선택적으로 가져옵니다.
            int nanoOfSecond = (asJsonArray.size() > 6) ? asJsonArray.get(6).getAsInt() : 0;

            return LocalDateTime.of(year, month, day, hour, minute, second, nanoOfSecond);
        }
        // 2. 입력값이 문자열일 수 있는 Primitive 타입인지 확인합니다.
        else if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
            return LocalDateTime.parse(json.getAsString(), FORMATTER);
        }

        // 3. 두 형식 모두 아니라면 예외를 발생시킵니다.
        throw new JsonParseException("LocalDateTime으로 변환할 수 없는 타입입니다: " + json);
    }

}
