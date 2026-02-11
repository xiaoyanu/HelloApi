package ee.zxz.helloapi.service;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface ApiService {

    // 获取API列表
    Map<String, Object> getApiList(Map<String, String> requestParam, HttpServletRequest request);

    // 创建API
    Map<String, Object> createApi(Map<String, Object> requestBody, HttpServletRequest request);

    // 更新API
    Map<String, Object> updateApi(int apiId, Map<String, Object> requestBody, HttpServletRequest request);

    // 删除API
    Map<String, Object> deleteApi(int apiId, HttpServletRequest request);
}
