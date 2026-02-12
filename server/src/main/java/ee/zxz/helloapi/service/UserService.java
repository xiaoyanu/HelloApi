package ee.zxz.helloapi.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface UserService {
    // 验证码生成
    void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception;

    // 用户注册
    Map<String, Object> register(Map<String, String> requestBody, HttpServletRequest request);

    // 用户登录
    Map<String, Object> login(Map<String, String> requestBody, HttpServletRequest request);

    // 用户信息
    Map<String, Object> getUserInfo(Map<String, String> requestParam, HttpServletRequest request);

    // 获取用户密钥
    Map<String, Object> getUserKey(Map<String, String> requestParam, HttpServletRequest request);

    // 获取用户API列表
    Map<String, Object> getUserApiList(Map<String, String> requestParam, HttpServletRequest request);

    // 删除用户
    Map<String, Object> deleteUser(String userId, HttpServletRequest request);
}