package ee.zxz.helloapi.utils;


import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseUtil {
    /**
     * 响应体构造方法
     * @param code 状态码
     * @param msg  消息
     * @param data 数据
     * @return 响应体
     */
    public static Map<String, Object> response(int code, String msg, Object data) {
        // 使用LinkedHashMap确保插入顺序
        Map<String, Object> result = new LinkedHashMap<>();
        // 首先添加code字段，确保它在JSON响应的最顶部
        result.put("code", code);
        result.put("msg", msg);
        if (data != null) {
            result.put("data", data);
        }
        return result;
    }

    /**
     * 响应体构造方法
     * @param code 状态码
     * @param msg  消息
     * @return 响应体
     */
    public static Map<String, Object> response(int code, String msg) {
        return response(code, msg, null);
    }

    /**
     * 成功响应体构造方法
     * @return 响应体
     */
    public static Map<String, Object> success() {
        return response(200, "成功");
    }

}
