package ee.zxz.helloapi.config;


import ee.zxz.helloapi.config.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 * 主要用于配置CORS跨域访问
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    public WebConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    /**
     * 配置拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册自定义拦截器
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**") // 拦截所有路径
                .excludePathPatterns("/error"); // 排除Spring默认错误路径
    }

    /**
     * 配置CORS跨域访问
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 允许所有路径进行跨域访问
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // 允许所有来源的跨域请求
                .allowedMethods("*")
                .allowCredentials(true)
                .maxAge(3600);
        
    }
}