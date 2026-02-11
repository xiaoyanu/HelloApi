package ee.zxz.helloapi.utils;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class Tools {

    /**
     * 生成32位MD5哈希值
     *
     * @param input 输入字符串
     * @return 32位MD5哈希值
     */
    public static String getTextMd5(String input) {
        // 处理null输入
        if (input == null) {
            input = "";
        }
        return DigestUtils.md5DigestAsHex(input.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 获取Token对应的用户ID
     *
     * @param authorizationHeader Authorization头信息
     * @return 用户ID(0表示无效Token)
     */
    public static int tokenToUserId(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return 0;
        }
        String token = authorizationHeader.substring(7);
        return JwtUtil.deToken(token, "id");
    }

    /**
     * 获取Token对应的用户权限
     *
     * @param authorizationHeader Authorization头信息
     * @return 用户权限 0-普通用户 1-管理员
     */
    public static int tokenToUserMode(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return 0;
        }
        String token = authorizationHeader.substring(7);
        return JwtUtil.deToken(token, "mode");
    }

}
