package ee.zxz.helloapi.controller;

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
    @PostMapping({"/log", "/log/"})
    public Map<String, Object> logApi(@RequestBody(required = false) Map<String, String> requestBody, HttpServletRequest request) {
        return statService.logApi(requestBody, request);
    }

    // Post - 获取指定类型数据 - POST
    @PostMapping("/")
    public Map<String, Object> GetStat(@RequestParam(required = false) Map<String, String> requestParam, @RequestBody(required = false) Map<String, String> requestBody, HttpServletRequest request, @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        return statService.getStat(requestParam, requestBody, request,authorizationHeader);
    }
}
