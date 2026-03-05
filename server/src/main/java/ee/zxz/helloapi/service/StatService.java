package ee.zxz.helloapi.service;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface StatService {

    // 获取指定类型数据
    Map<String, Object> getStat(Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request);

    // 记录API日志/消耗等
    Map<String, Object> logApi(Map<String, String> requestBody, HttpServletRequest request);
}
