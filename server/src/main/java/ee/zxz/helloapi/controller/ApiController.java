package ee.zxz.helloapi.controller;

import ee.zxz.helloapi.service.ApiService;
import ee.zxz.helloapi.utils.Finals;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(Finals.apiUrl)
public class ApiController {
    private final ApiService apiService;
    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }
    // Register - 注册账号 - POST
    @GetMapping("/GetApiList")
    public Map<String, Object> GetApiList(@RequestParam(required = false) Map<String, String> requestBody, HttpServletRequest request) {
        return apiService.getApiList(requestBody, request);
    }
}
