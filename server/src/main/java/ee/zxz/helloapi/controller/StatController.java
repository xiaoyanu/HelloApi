package ee.zxz.helloapi.controller;

import ee.zxz.helloapi.annotation.RequiresLogin;
import ee.zxz.helloapi.domain.ApiRequestLog;
import ee.zxz.helloapi.service.Impl.StatServiceImpl;
import ee.zxz.helloapi.utils.Finals;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(Finals.statUrl)
public class StatController {
    private final StatServiceImpl statService;

    public StatController(StatServiceImpl statService) {
        this.statService = statService;
    }

    // LogApi - 记录API日志/消耗等 - POST
    @PostMapping({"/log/{userKey}/{key}", "/log/", "/log/{userKey}", "/log/{userKey}/"})
    public Map<String, Object> LogApi(@PathVariable(required = false) String userKey, @PathVariable(required = false) String key, @RequestBody ApiRequestLog apiRequestLog, HttpServletRequest request) {
        return statService.logApi(userKey, key, apiRequestLog, request);
    }

    // Post - 获取指定类型数据 - Post
    @PostMapping("/")
    @RequiresLogin()
    public Map<String, Object> GetStat(@RequestParam(required = false) Map<String, String> requestParam, @RequestBody(required = false) Map<String, String> requestBody, HttpServletRequest request) {
        return statService.getStat(requestParam, requestBody, request);
    }
}
