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

    // 重置用户密钥
    Map<String, Object> resetUserKey(String userId, HttpServletRequest request);

    // 用户API列表搜索
    Map<String, Object> userApiListSearch(Map<String, String> requestParam, HttpServletRequest request);

    // 更新用户昵称
    Map<String, Object> updateUserNick(Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request);

    // 更新用户邮箱
    Map<String, Object> updateUserMail(Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request);

    // 更新用户密码
    Map<String, Object> updateUserPassword(Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request);

    // 设置用户密码
    Map<String, Object> setUserPassword(Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request);

    // 获取用户列表
    Map<String, Object> getUserList(Map<String, String> requestParam, HttpServletRequest request);

    // 用户列表搜索
    Map<String, Object> userListSearch(Map<String, String> requestParam, HttpServletRequest request);

    // 设置用户权限
    Map<String, Object> setUserMode(Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request);

    // 更改全局设置
    Map<String, Object> updateSettingValue(Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request);

    // 获取设置参数
    Map<String, Object> getSetting(Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request);
}