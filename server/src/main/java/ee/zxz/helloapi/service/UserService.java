package ee.zxz.helloapi.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface UserService {
    // 处理验证码生成
    void generateCaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception;

    // 处理用户注册逻辑
    Map<String, Object> register(Map<String, String> requestBody, HttpServletRequest request);
}