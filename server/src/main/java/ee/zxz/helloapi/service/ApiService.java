package ee.zxz.helloapi.service;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface ApiService {
    // 获取API列表 - GET
    Map<String, Object> getApiList(Map<String, String> requestBody, HttpServletRequest request);
}
