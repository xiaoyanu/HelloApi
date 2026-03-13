package ee.zxz.helloapi.service;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface SettingService {
    Map<String, Object> getSettingList(Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request);

    Map<String, Object> updateSettingValue(Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request);

    Map<String, Object> getSetting(String settingKey, Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request);
}
