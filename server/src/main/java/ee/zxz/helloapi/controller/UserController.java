package ee.zxz.helloapi.controller;

import ee.zxz.helloapi.annotation.RequiresLogin;
import ee.zxz.helloapi.service.UserService;
import ee.zxz.helloapi.utils.Finals;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(Finals.userUrl)
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Captcha - 获取验证码图片 - GET
    @GetMapping("/Captcha")
    public void GetCaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        userService.getCaptcha(request, response);
    }

    // Register - 注册账号 - POST
    @PostMapping("/Register")
    public Map<String, Object> Register(@RequestBody(required = false) Map<String, String> requestBody, HttpServletRequest request) {
        return userService.register(requestBody, request);
    }

    // Login - 登录账号 - POST
    @PostMapping("/Login")
    public Map<String, Object> Login(@RequestBody(required = false) Map<String, String> requestBody, HttpServletRequest request) {
        return userService.login(requestBody, request);
    }

    // GetUserInfo - 获取用户信息 - GET
    @GetMapping("/")
    @RequiresLogin
    public Map<String, Object> GetUserInfo(@RequestParam(required = false) Map<String, String> requestParam, HttpServletRequest request) {
        return userService.getUserInfo(requestParam, request);
    }

    // GetUserKey - 获取用户密钥 - GET
    @GetMapping("/key")
    @RequiresLogin
    public Map<String, Object> GetUserKey(@RequestParam(required = false) Map<String, String> requestParam, HttpServletRequest request) {
        return userService.getUserKey(requestParam, request);
    }

    // DeleteUser - 删除用户 - DELETE
    @DeleteMapping("/")
    @RequiresLogin(mode = Finals.Admin)
    public Map<String, Object> DeleteUser(@RequestBody(required = false) Map<String, String> requestBody, HttpServletRequest request) {
        return userService.deleteUser(requestBody, request);
    }

    // GetUserApiList - 获取用户API列表 - GET
    @GetMapping("/AppList")
    @RequiresLogin
    public Map<String, Object> GetUserApiList(@RequestParam(required = false) Map<String, String> requestParam, HttpServletRequest request) {
        return userService.getUserApiList(requestParam, request);
    }

}
