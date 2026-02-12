package ee.zxz.helloapi.service;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface ApiService {

    // 获取API列表
    Map<String, Object> getApiList(Map<String, String> requestParam, HttpServletRequest request);

    // 创建API
    Map<String, Object> createApi(Map<String, Object> requestBody, HttpServletRequest request);

    // 更新API
    Map<String, Object> updateApi(String apiId, Map<String, Object> requestBody, HttpServletRequest request);

    // 删除API
    Map<String, Object> deleteApi(String apiId, HttpServletRequest request);

    // 搜索API
    Map<String, Object> searchApiList(Map<String, String> requestParam);

    // 获取API应用详情
    Map<String, Object> getApiApp(String apiId);

    // 删除API参数
    Map<String, Object> deleteApiParam(String apiID, Map<String, String> requestBody, HttpServletRequest request);
}
