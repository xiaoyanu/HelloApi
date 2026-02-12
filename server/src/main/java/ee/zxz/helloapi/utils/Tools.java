package ee.zxz.helloapi.utils;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

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

    /**
     * 根据对象中的某个属性对 List 进行去重（保留第一次出现的对象）
     *
     * @param list         需要去重的 List
     * @param keyExtractor 提取去重关键字的函数 (例如: 对象::属性的Get方法)
     * @param <T>          List 中对象的真实类型
     * @param <K>          去重依据的属性类型
     * @return 去重后的新 List
     */
    public static <T, K> List<T> deleteDuplicatesList(List<T> list, Function<? super T, ? extends K> keyExtractor) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        Set<K> seen = new HashSet<>();
        List<T> result = new ArrayList<>();
        for (T item : list) {
            // keyExtractor.apply(item) 会获取指定属性的值
            // seen.add() 如果元素已存在返回 false，不存在返回 true
            if (seen.add(keyExtractor.apply(item))) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * 获取分页偏移量
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return 偏移量
     */
    public static int getPageOffset(int page, int pageSize) {
        return (page - 1) * pageSize;
    }


    /**
     * 字符串转整数
     *
     * @param str 字符串
     * @return 失败返回-1
     */
    public static int strToInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}
