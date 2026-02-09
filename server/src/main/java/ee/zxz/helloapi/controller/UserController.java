package ee.zxz.helloapi.controller;

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
    public void Captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        userService.generateCaptcha(request, response);
    }

    // Register - 注册账号 - POST
    @PostMapping("/Register")
    public Map<String, Object> Register(@RequestBody(required = false) Map<String, String> requestBody, HttpServletRequest request) {
        return userService.register(requestBody, request);
    }
}
