package ee.zxz.helloapi.service;

import ee.zxz.helloapi.domain.ApiRequestLog;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface StatService {

    // 记录API日志/消耗等
    Map<String, Object> logApi(String userKey, String key, ApiRequestLog apiRequestLog, HttpServletRequest request);

    // 获取指定类型数据
    Map<String, Object> getStat(Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request);
}
