package ee.zxz.helloapi.controller;

import ee.zxz.helloapi.annotation.RequiresLogin;
import ee.zxz.helloapi.service.SettingService;
import ee.zxz.helloapi.utils.Finals;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(Finals.settingUrl)
public class SettingController {
    private final SettingService settingService;

    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    // GetSettingList - 获取所有设置列表 - GET
    @GetMapping("/list")
    @RequiresLogin(mode = Finals.Admin)
    public Map<String, Object> GetSettingList(@RequestParam(required = false) Map<String, String> requestParam, @RequestBody(required = false) Map<String, String> requestBody, HttpServletRequest request) {
        return settingService.getSettingList(requestParam, requestBody, request);
    }

    // GetSetting - 获取指定设置 - GET
    @GetMapping("/{settingKey}")
    public Map<String, Object> GetSetting(@PathVariable(required = false) String settingKey, @RequestParam(required = false) Map<String, String> requestParam, @RequestBody(required = false) Map<String, String> requestBody, HttpServletRequest request) {
        return settingService.getSetting(settingKey, requestParam, requestBody, request);
    }

    // UpdateSettingValue - 更新设置 - PUT
    @PutMapping("/")
    @RequiresLogin(mode = Finals.Admin)
    public Map<String, Object> UpdateSettingValue(@RequestParam(required = false) Map<String, String> requestParam, @RequestBody(required = false) Map<String, String> requestBody, HttpServletRequest request) {
        return settingService.updateSettingValue(requestParam, requestBody, request);
    }


}
