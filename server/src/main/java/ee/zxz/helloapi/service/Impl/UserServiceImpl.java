package ee.zxz.helloapi.service.Impl;

import com.pig4cloud.captcha.GifCaptcha;
import com.pig4cloud.captcha.SpecCaptcha;
import com.pig4cloud.captcha.base.Captcha;
import com.pig4cloud.captcha.utils.CaptchaJakartaUtil;
import ee.zxz.helloapi.domain.ApiApp;
import ee.zxz.helloapi.domain.User;
import ee.zxz.helloapi.domain.UserKey;
import ee.zxz.helloapi.mapper.ApiMapper;
import ee.zxz.helloapi.mapper.UserMapper;
import ee.zxz.helloapi.service.UserService;
import ee.zxz.helloapi.utils.Finals;
import ee.zxz.helloapi.utils.JwtUtil;
import ee.zxz.helloapi.utils.ResponseUtil;
import ee.zxz.helloapi.utils.Tools;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final ApiMapper apiMapper;

    public UserServiceImpl(UserMapper userMapper, ApiMapper apiMapper) {
        this.userMapper = userMapper;
        this.apiMapper = apiMapper;
    }

    @Override
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        GifCaptcha gifCaptcha = new GifCaptcha(120, 32, 4);
        gifCaptcha.setCharType(Captcha.TYPE_NUM_AND_UPPER);

        SpecCaptcha captcha = new SpecCaptcha(120, 32, 4);
        captcha.setCharType(Captcha.TYPE_NUM_AND_UPPER);

        if (Math.random() > 0.5) {
            CaptchaJakartaUtil.out(gifCaptcha, request, response);
        } else {
            CaptchaJakartaUtil.out(captcha, request, response);
        }
    }

    @Override
    public Map<String, Object> register(Map<String, String> requestBody, HttpServletRequest request) {
        // 1. 基础非空校验
        if (requestBody == null || requestBody.isEmpty()) {
            return ResponseUtil.response(400, "请求参数不能为空");
        }

        String name = requestBody.get("name");
        String password = requestBody.get("password");
        String code = requestBody.get("code");
        String mail = requestBody.get("mail");


        // 参数校验
        if (name == null || name.trim().isEmpty()) {
            return ResponseUtil.response(400, "账号不能为空");
        }
        if (name.equals(password)) {
            return ResponseUtil.response(400, "账号和密码不能相同");
        }
        if (name.length() < 4) {
            return ResponseUtil.response(400, "账号不能少于4个字符");
        }
        if (password == null || password.trim().isEmpty()) {
            return ResponseUtil.response(400, "密码不能为空");
        }
        if (password.length() < 6) {
            return ResponseUtil.response(400, "密码不能少于6个字符");
        }
        // 检查密码是否包含空格
        if (password.contains(" ")) {
            return ResponseUtil.response(400, "密码不能包含空格");
        }
        // 检查用户名是否包含空格
        if (name.contains(" ")) {
            return ResponseUtil.response(400, "账号不能包含空格");
        }
        if (name.length() > 32) {
            return ResponseUtil.response(400, "账号不能超过32个字符");
        }
        if (password.length() > 64) {
            return ResponseUtil.response(400, "密码不能超过64个字符");
        }
        if (code == null || code.isEmpty()) {
            return ResponseUtil.response(400, "验证码不能为空");
        }
        if (code.length() != 4) {
            return ResponseUtil.response(400, "验证码长度必须为4个字符");
        }
        if (mail == null || mail.trim().isEmpty()) {
            return ResponseUtil.response(400, "邮箱不能为空");
        }
        if (!mail.matches("^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$")) {
            return ResponseUtil.response(400, "邮箱格式错误");
        }

        // 3. 验证码校验
        if (!CaptchaJakartaUtil.ver(code, request)) {
            return ResponseUtil.response(400, "验证码错误/过期，请重新输入");
        }
        CaptchaJakartaUtil.clear(request);

        try {
            // 4. 数据库校验与写入
            if (userMapper.checkUsernameExists(name) > 0) {
                return ResponseUtil.response(400, "账号已存在");
            }

            String md5Password = Tools.getTextMd5(password);
            int result;
            if (userMapper.getUserCount() < 1) {
                // 第一个注册用户为管理员
                result = userMapper.register(name, md5Password, name, 1, mail);
            } else {
                result = userMapper.register(name, md5Password, name, 0, mail);
            }

            if (result <= 0) {
                return ResponseUtil.response(500, "注册失败，数据库插入失败");
            }

            return ResponseUtil.response(200, "注册成功");
        } catch (Exception e) {
            return ResponseUtil.response(500, "系统繁忙：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> login(Map<String, String> requestBody, HttpServletRequest request) {

        // 获取JSON中的username和password
        String name = requestBody.get("name");
        String password = requestBody.get("password");
        String code = requestBody.get("code");
        // 参数校验
        if (name == null || name.trim().isEmpty()) {
            return ResponseUtil.response(400, "账号不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            return ResponseUtil.response(400, "密码不能为空");
        }
        if (name.contains(" ")) {
            return ResponseUtil.response(400, "账号不能包含空格");
        }
        if (password.contains(" ")) {
            return ResponseUtil.response(400, "密码不能包含空格");
        }
        if (name.length() > 32) {
            return ResponseUtil.response(400, "账号不能超过32个字符");
        }
        if (name.length() < 4) {
            return ResponseUtil.response(400, "账号不能少于4个字符");
        }
        if (password.length() < 6) {
            return ResponseUtil.response(400, "密码不能少于6个字符");
        }
        if (password.length() > 64) {
            return ResponseUtil.response(400, "密码不能超过64个字符");
        }
        if (code == null || code.isEmpty()) {
            return ResponseUtil.response(400, "验证码不能为空");
        }
        if (code.length() != 4) {
            return ResponseUtil.response(400, "验证码长度必须为4个字符");
        }

        // 验证验证码
        if (!CaptchaJakartaUtil.ver(code, request)) {
            CaptchaJakartaUtil.clear(request);  // 清除session中的验证码
            return ResponseUtil.response(400, "验证码错误/过期，请重新输入");
        }
        CaptchaJakartaUtil.clear(request);  // 清除session中的验证码

        password = Tools.getTextMd5(password);

        if (userMapper.checkUsernameExists(name) <= 0) {
            return ResponseUtil.response(400, "账号不存在");
        }

        // 获取用户信息
        User user = userMapper.login(name, password);
        if (user == null) {
            return ResponseUtil.response(400, "密码错误");
        }

        // 生成JWT token
        String token = JwtUtil.getToken(user.getId(), user.getName(), user.getMode());

        // 构建返回
        Map<String, Object> data = new LinkedHashMap<>();
        Map<String, Object> info = new LinkedHashMap<>();
        data.put("token", token);
        data.put("user", info);
        info.put("id", user.getId());
        info.put("name", user.getName());
        info.put("nick", user.getNick());
        info.put("mode", user.getMode());
        info.put("mail", user.getMail());
        return ResponseUtil.response(200, "登陆成功", data);
    }

    @Override
    public Map<String, Object> getUserInfo(Map<String, String> requestParam, HttpServletRequest request) {
        int userId = (int) request.getAttribute("userId");
        if (userMapper.checkUserIdExists(userId) < 1) {
            return ResponseUtil.response(401, Finals.MESSAGES_ERROR_USER_NOT_FOUND);
        }
        if (requestParam.get("id") != null && !requestParam.get("id").trim().isEmpty()) {
            // 检查Token是否为管理员
            if ((int) request.getAttribute("userMode") != Finals.Admin) {
                return ResponseUtil.response(403, Finals.MESSAGES_ERROR_NOT_ADMIN);
            }
            userId = Tools.strToInt(requestParam.get("id"));
        }

        // 获取用户信息
        User user = userMapper.getUserInfo(userId);
        if (user == null) {
            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_USER_NOT_FOUND);
        }

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", user.getId());
        data.put("name", user.getName());
        data.put("nick", user.getNick());
        data.put("mode", user.getMode());
        data.put("mail", user.getMail());

        return ResponseUtil.response(200, "获取成功", data);
    }

    @Override
    public Map<String, Object> getUserKey(Map<String, String> requestParam, HttpServletRequest request) {
        int userId = (int) request.getAttribute("userId");
        if (userMapper.checkUserIdExists(userId) < 1) {
            return ResponseUtil.response(401, Finals.MESSAGES_ERROR_USER_NOT_FOUND);
        }
        UserKey user_key = userMapper.getUserKey(userId);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", userId);
        if (user_key == null) {
            data.put("key", "");
        } else {
            data.put("key", user_key.getKey());
        }

        return ResponseUtil.response(200, "获取成功", data);
    }

    @Override
    public Map<String, Object> deleteUser(String userId, HttpServletRequest request) {
        try {
            int intUserId = Tools.strToInt(userId);
            if (intUserId != 0) {
                if (intUserId == (int) request.getAttribute("userId")) {
                    return ResponseUtil.response(403, "你不能删除自己");
                }
                // 检查用户ID是否存在
                if (userMapper.checkUserIdExists(intUserId) < 1) {
                    return ResponseUtil.response(400, Finals.MESSAGES_ERROR_USER_NOT_FOUND);
                }
                // 删除用户
                userMapper.deleteUser(intUserId);
                // 删除用户密钥
                userMapper.deleteUserKey(intUserId);
                // 删除用户API
                List<ApiApp> apiAppList = userMapper.getUserApiListAll(intUserId);
                if (!apiAppList.isEmpty()) {
                    for (ApiApp apiApp : apiAppList) {
                        // 删除API参数
                        apiMapper.deleteApiParamAll(apiApp.getId());
                        // 删除API应用
                        apiMapper.deleteApiApp(apiApp.getId());
                    }
                }
                return ResponseUtil.success();
            } else {
                return ResponseUtil.response(400, Finals.MESSAGES_ERROR_PARAM);
            }
        } catch (Exception e) {
            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_PARAM);
        }
    }

    @Override
    public Map<String, Object> getUserApiList(Map<String, String> requestParam, HttpServletRequest request) {
        int userId = (int) request.getAttribute("userId");
        if (userMapper.checkUserIdExists(userId) < 1) {
            return ResponseUtil.response(401, Finals.MESSAGES_ERROR_USER_NOT_FOUND);
        }

        int pageSize = Tools.strToInt(requestParam.get("pageSize"));
        int page = Tools.strToInt(requestParam.get("page"));
        if (pageSize < 1) {
            pageSize = 30;
        }
        if (page < 1) {
            page = 1;
        }

        // 如果携带了ID，则验证是不是管理员，只有管理才可以带ID
        if (requestParam.get("id") != null && !requestParam.get("id").trim().isEmpty()) {
            if ((int) request.getAttribute("userMode") != Finals.Admin) {
                return ResponseUtil.response(403, Finals.MESSAGES_ERROR_NOT_ADMIN);
            }
            userId = Tools.strToInt(requestParam.get("id"));
            if (userMapper.checkUserIdExists(userId) < 1) {
                return ResponseUtil.response(400, Finals.MESSAGES_ERROR_USER_NOT_FOUND);
            }
        }

        // 获取用户API列表
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (ApiApp apiApp : userMapper.getUserApiList(userId, pageSize, Tools.getPageOffset(page, pageSize))) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", apiApp.getId());
            map.put("title", apiApp.getTitle());
            map.put("type", apiApp.getType());
            map.put("status", apiApp.getStatus());
            map.put("created", apiApp.getCreated());
            map.put("user_id", apiApp.getUser_id());
            resultList.add(map);
        }
        return ResponseUtil.response(200, "获取成功", resultList);
    }

    @Override
    public Map<String, Object> resetUserKey(String userId, HttpServletRequest request) {
        int intUserId = Tools.strToInt(userId);
        int tokenUserId = (int) request.getAttribute("userId");
        if (intUserId != tokenUserId) {
            if ((int) request.getAttribute("userMode") != Finals.Admin) {
                return ResponseUtil.response(403, Finals.MESSAGES_ERROR_NOT_ADMIN);
            }
        }
        // 验证用户ID是否存在
        if (userMapper.checkUserIdExists(intUserId) < 1) {
            return ResponseUtil.response(400, Finals.MESSAGES_ERROR_USER_NOT_FOUND);
        }

        // key 生成
        String key = Tools.getTextMd5(String.valueOf(intUserId) + System.currentTimeMillis());

        // 重置用户密钥
        userMapper.resetUserKey(intUserId, key, System.currentTimeMillis());
        return ResponseUtil.success(key);
    }
}