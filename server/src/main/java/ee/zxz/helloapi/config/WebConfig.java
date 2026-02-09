package ee.zxz.helloapi.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 * 主要用于配置CORS跨域访问
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 配置CORS跨域访问
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 允许所有路径进行跨域访问
        registry.addMapping("/**")
                /*
                  # 请将伪静态规则或自定义NGINX配置填写到此处
                  add_header 'Access-Control-Allow-Origin' 'https://pm.zxz.ee' always;
                  add_header 'Access-Control-Allow-Methods' 'GET, POST, PUT, DELETE, OPTIONS' always;
                  add_header 'Access-Control-Allow-Headers' '*' always;
                  add_header 'Access-Control-Allow-Credentials' 'true' always;
                 */

                // 允许的源模式
                .allowedOriginPatterns("http://localhost:*", "http://127.0.0.1:*", "http://*.zxz.ee", "https://*.zxz.ee")
                // 允许的请求方法
                .allowedMethods("GET", "POST",  "OPTIONS")
                // 允许的请求头
                .allowedHeaders("*")
                // 允许暴露的响应头
                .exposedHeaders("*")
                // 允许携带cookie
                .allowCredentials(true)
                // 设置预检请求结果的缓存时间（秒）
                .maxAge(3600);
    }
}