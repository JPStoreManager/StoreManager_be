package manage.store.utils;

import com.google.common.collect.ImmutableMap;
import manage.store.exception.common.InvalidParameterException;

public class ApiPathUtils {

    private static final String API_PREFIX = "/api/v1";

    public interface ApiPath {
        class User {
            public static final String USER_BASIC_PREFIX = API_PREFIX + "/user";

            public static final String LOGIN = USER_BASIC_PREFIX + "/login";
            public static final String LOGOUT = USER_BASIC_PREFIX + "/logout";

            public static class FindPassword {
                private static final String FIND_PW_BASIC_PREFIX = API_PREFIX + USER_BASIC_PREFIX + "/find/pw";

                public static final String SEND_OTP = FIND_PW_BASIC_PREFIX + "/otp/send";
                public static final String VALIDATE_OTP = FIND_PW_BASIC_PREFIX + "/otp/verify";
                public static final String UPDATE_PW = FIND_PW_BASIC_PREFIX + "";
            }
        }
        class Sales {
            private static final String SALES_BASIC_PREFIX = API_PREFIX + "/sales";
            
            public static final String SALES_PAGE_OPTIONS = SALES_BASIC_PREFIX + "/options";
            public static final String SALES_MONTH = SALES_BASIC_PREFIX + "/month";
            public static final String SALES_YEAR = SALES_BASIC_PREFIX + "/year";
        }
    }
    
    public static class ApiName {
        
        private final String name;
        
        private ApiName(String name) {
            this.name = name;
        }
        
        // User
        public static ApiName LOGIN = new ApiName("user.login");
        public static ApiName LOGOUT = new ApiName("user.logout");
        public static ApiName FIND_PW_SEND_OTP = new ApiName("user.find.pw.sendOtp");
        public static ApiName FIND_PW_VALIDATE_OTP = new ApiName("user.find.pw.validateOtp");
        public static ApiName FIND_PW_UPDATE_PW = new ApiName("user.find.pw.updatePw");
        
        // Sales
        public static ApiName SALES_PAGE_OPTIONS = new ApiName("sales.pageOptions");
        public static ApiName SALES_MONTH = new ApiName("sales.month");
        public static ApiName SALES_YEAR = new ApiName("sales.year");
    }

    private static final ImmutableMap<ApiName, String> apiPathByName = ImmutableMap.<ApiName, String>builder()
            // User API
            .put(ApiName.LOGIN, ApiPath.User.LOGIN)
            .put(ApiName.FIND_PW_SEND_OTP, ApiPath.User.FindPassword.SEND_OTP)
            .put(ApiName.FIND_PW_VALIDATE_OTP, ApiPath.User.FindPassword.VALIDATE_OTP)
            .put(ApiName.FIND_PW_UPDATE_PW, ApiPath.User.FindPassword.UPDATE_PW)
            
            // Sales API
            .put(ApiName.SALES_PAGE_OPTIONS, ApiPath.Sales.SALES_PAGE_OPTIONS)
            .put(ApiName.SALES_MONTH, ApiPath.Sales.SALES_MONTH)
            .put(ApiName.SALES_YEAR, ApiPath.Sales.SALES_YEAR)
            .build();
    

    /**
     * apiName에 해당하는 path를 반환한다.
     * @param apiName path를 가져올 apiName
     * @return apiName에 해당하는 path
     * @throws RuntimeException apiName이 null인 경우
     */
    public static String getPath(ApiName apiName) {
        if(apiName == null) throw new InvalidParameterException("apiName must not be null");

        return apiPathByName.get(apiName);
    }
}
