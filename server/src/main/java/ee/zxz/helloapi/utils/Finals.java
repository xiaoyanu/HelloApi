package ee.zxz.helloapi.utils;

public class Finals {
    // 私有构造函数，防止实例化
    private Finals() {
        throw new AssertionError("禁止实例化此类");
    }

    public static final String userUrl = "/api/v1/user";
    public static final String apiUrl = "/api/v1/app";
    public static final String MESSAGES_TOKEN_TIME_OUT = "Token过期/无效";
    public static final String MESSAGES_ERROR_NOT_ADMIN = "非管理员用户，没有操作权限";
    public static final String MESSAGES_ERROR_PARAM = "参数错误";
    public static final String MESSAGES_ERROR_USER_NOT_FOUND = "用户不存在";
    public static final String MESSAGES_ERROR_NOT_CREATOR = "您不是创建人/管理员，没有操作权限";
    public static final String MESSAGES_ERROR_API_NOT_FOUND = "API不存在";
    public static final int Admin = 1;

}
