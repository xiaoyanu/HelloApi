package ee.zxz.helloapi.service.impl;

import ee.zxz.helloapi.domain.Setting;
import ee.zxz.helloapi.mapper.SettingMapper;
import ee.zxz.helloapi.service.SettingService;
import ee.zxz.helloapi.utils.Finals;
import ee.zxz.helloapi.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SettingServiceImpl implements SettingService {
    private final SettingMapper settingMapper;

    public SettingServiceImpl(SettingMapper settingMapper) {
        this.settingMapper = settingMapper;
    }

    @Override
    public Map<String, Object> getSettingList(Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request) {
        List<Map<String, Object>> settingList = new ArrayList<>();
        for (Setting setting : settingMapper.getSettingValueAll()) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("key", setting.getKey());
            map.put("value", setting.getValue());
            settingList.add(map);

        }
        return ResponseUtil.success(settingList);
    }

    @Override
    public Map<String, Object> updateSettingValue(Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request) {
        String key = requestBody.get("key");
        String value = requestBody.get("value");
        if (key == null || value == null || key.isEmpty() || value.isEmpty()) {
            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_PARAM);
        }
        settingMapper.updateSettingValue(key, value);
        return ResponseUtil.success();
    }

    @Override
    public Map<String, Object> getSetting(String settingKey, Map<String, String> requestParam, Map<String, String> requestBody, HttpServletRequest request) {
        switch (settingKey) {
            case "link_name", "link_desc", "link_url", "link_icon", "link_email":
                break;
            default:
                return ResponseUtil.response(400, Finals.MESSAGES_ERROR_PARAM);
        }
        return ResponseUtil.success(settingMapper.getSettingValue(settingKey));
    }
}
