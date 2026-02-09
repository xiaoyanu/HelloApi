package ee.zxz.helloapi.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        try {
            // 获取MD5实例
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算MD5哈希值
            byte[] messageDigest = md.digest(input.getBytes());
            // 使用字符数组预分配空间，避免重复拼接字符串
            char[] hexArray = "0123456789abcdef".toCharArray();
            char[] hexChars = new char[32]; // MD5固定32位

            // 转换字节数组为16进制字符串
            for (int i = 0; i < messageDigest.length; i++) {
                int v = messageDigest[i] & 0xFF;
                hexChars[i * 2] = hexArray[v >>> 4]; // 高4位
                hexChars[i * 2 + 1] = hexArray[v & 0x0F]; // 低4位
            }

            return new String(hexChars);
        } catch (NoSuchAlgorithmException e) {
            // MD5算法几乎在所有Java环境中都可用，抛出运行时异常
            throw new RuntimeException("无法初始化MD5算法", e);
        }
    }

}
