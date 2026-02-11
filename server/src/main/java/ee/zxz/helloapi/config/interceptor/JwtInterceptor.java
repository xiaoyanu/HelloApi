package ee.zxz.helloapi.config.interceptor;

import com.alibaba.fastjson.JSON;
import ee.zxz.helloapi.annotation.RequiresLogin;
import ee.zxz.helloapi.utils.Finals;
import ee.zxz.helloapi.utils.ResponseUtil;
import ee.zxz.helloapi.utils.Tools;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

@Component
public class JwtInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 如果不是映射到方法（比如静态资源），直接放行
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        Method method = handlerMethod.getMethod();


        if (method.isAnnotationPresent(RequiresLogin.class)) {
            int userId = Tools.tokenToUserId(request.getHeader("Authorization"));
            String json;
            if (userId < 1) {
                response.setContentType("application/json;charset=UTF-8");
                json = JSON.toJSONString(ResponseUtil.response(401, Finals.MESSAGES_TOKEN_TIME_OUT));
                response.getWriter().write(json);
                return false;
            }

            // 如果定义了mode的话，验证是否为管理员
            RequiresLogin annotation = method.getAnnotation(RequiresLogin.class);
            int userMode = Tools.tokenToUserMode(request.getHeader("Authorization"));
            if (annotation.mode() != 0) {
                if (userMode != Finals.Admin) {
                    response.setContentType("application/json;charset=UTF-8");
                    json = JSON.toJSONString(ResponseUtil.response(403, Finals.MESSAGES_ERROR_NOT_ADMIN));
                    response.getWriter().write(json);
                    return false;
                }
            }

            // 把计算过的userId和userMode放到request中
            request.setAttribute("userId", userId);
            request.setAttribute("userMode", userMode);
        }

        return true;
    }
}