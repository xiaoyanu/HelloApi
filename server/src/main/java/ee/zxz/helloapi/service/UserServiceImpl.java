package ee.zxz.helloapi.service;

import com.pig4cloud.captcha.GifCaptcha;
import com.pig4cloud.captcha.SpecCaptcha;
import com.pig4cloud.captcha.base.Captcha;
import com.pig4cloud.captcha.utils.CaptchaJakartaUtil;
import ee.zxz.helloapi.mapper.UserMapper;
import ee.zxz.helloapi.utils.ResponseUtil;
import ee.zxz.helloapi.utils.Tools;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public void generateCaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
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

        // 2. 业务规则校验 (建议此处可提取专门的 Validator 类)
        Map<String, Object> validationResult = validateRegisterParams(name, password, code);
        if (validationResult != null) return validationResult;

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
            int result = userMapper.register(name, md5Password, name, 0);

            if (result <= 0) {
                return ResponseUtil.response(500, "注册失败，数据库插入失败");
            }

            return ResponseUtil.response(200, "注册成功");
        } catch (Exception e) {
            return ResponseUtil.response(500, "系统繁忙：" + e.getMessage());
        }
    }

    /**
     * 私有辅助方法：参数合规性检查
     */
    private Map<String, Object> validateRegisterParams(String name, String password, String code) {
        if (name == null || name.trim().isEmpty()) return ResponseUtil.response(400, "账号不能为空");
        if (name.contains(" ") || name.length() < 4 || name.length() > 32)
            return ResponseUtil.response(400, "账号格式不正确");
        if (password == null || password.trim().isEmpty()) return ResponseUtil.response(400, "密码不能为空");
        if (password.contains(" ") || password.length() < 6 || password.length() > 64)
            return ResponseUtil.response(400, "密码格式不正确");
        if (name.equals(password)) return ResponseUtil.response(400, "账号和密码不能相同");
        if (code == null || code.length() != 4) return ResponseUtil.response(400, "验证码长度必须为4个字符");
        return null;
    }
}
