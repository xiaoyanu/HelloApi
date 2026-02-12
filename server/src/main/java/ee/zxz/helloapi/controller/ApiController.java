package ee.zxz.helloapi.controller;

import ee.zxz.helloapi.annotation.RequiresLogin;
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

    // GetApiList - 获取API列表 - GET
    @GetMapping("/list")
    public Map<String, Object> GetApiList(@RequestParam(required = false) Map<String, String> requestParam, HttpServletRequest request) {
        return apiService.getApiList(requestParam, request);
    }

    // CreateApi - 创建API - POST
    @PostMapping("/")
    @RequiresLogin
    public Map<String, Object> CreateApi(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) {
        return apiService.createApi(requestBody, request);
    }

    // UpdateApi - 更新API - PUT
    @PutMapping("/")
    @RequiresLogin
    public Map<String, Object> UpdateApi(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) {
        return apiService.updateApi("0", requestBody, request);
    }

    @PutMapping("/{apiId}")
    @RequiresLogin
    public Map<String, Object> UpdateApi(@PathVariable("apiId") String apiId, @RequestBody Map<String, Object> requestBody, HttpServletRequest request) {
        return apiService.updateApi(apiId, requestBody, request);
    }

    // DeleteApi - 删除API - DELETE
    @DeleteMapping("/")
    @RequiresLogin
    public Map<String, Object> DeleteApi(HttpServletRequest request) {
        return apiService.deleteApi("0", request);
    }

    @DeleteMapping("/{apiId}")
    @RequiresLogin
    public Map<String, Object> DeleteApi(@PathVariable("apiId") String apiId, HttpServletRequest request) {
        return apiService.deleteApi(apiId, request);
    }

    // SearchApiList - 搜索Api接口 - GET
    @GetMapping("/search")
    public Map<String, Object> SearchApiList(@RequestParam Map<String, String> requestParam) {
        return apiService.searchApiList(requestParam);
    }

    // GetApiApp - 获取Api接口详情 - GET
    @GetMapping("/")
    public Map<String, Object> GetApiApp() {
        return apiService.getApiApp("0");
    }

    @GetMapping("/{apiId}")
    public Map<String, Object> GetApiApp(@PathVariable("apiId") String apiId) {
        return apiService.getApiApp(apiId);
    }

    // DeleteApiParam - 删除API参数 - DELETE
    @DeleteMapping("/param/")
    @RequiresLogin
    public Map<String, Object> DeleteApiParam(@RequestBody Map<String, String> requestBody, HttpServletRequest request) {
        return apiService.deleteApiParam("0", requestBody, request);
    }
    @DeleteMapping("/param/{apiID}")
    @RequiresLogin
    public Map<String, Object> DeleteApiParam(@PathVariable("apiID") String apiID,@RequestBody Map<String, String> requestBody, HttpServletRequest request) {
        return apiService.deleteApiParam(apiID, requestBody, request);
    }

}
